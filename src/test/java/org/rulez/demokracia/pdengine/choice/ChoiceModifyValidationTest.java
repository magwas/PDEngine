package org.rulez.demokracia.pdengine.choice;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;

@TestedFeature("Manage votes")
@TestedOperation("modify vote")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ChoiceModifyValidationTest extends ChoiceTestBase {

  private static final String VALIDATES_INPUTS = "validates inputs";
  private static final String NEW_CHOICE_NAME = "NewChoiceName";

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_voteId_is_rejected() {
    assertThrows(
        () -> choiceService
            .modifyChoice(
                voteData.adminInfoWithInvalidVoteId,
                voteData.choice.getId(), NEW_CHOICE_NAME
            )
    ).assertMessageIs(ILLEGAL_VOTE_ID);
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_choiceId_is_rejected() {
    final String invalidChoiceId = "invalidChoiceId";

    assertThrows(
        () -> choiceService
            .modifyChoice(
                voteData.adminInfoWithNoNeededAssurances, invalidChoiceId,
                NEW_CHOICE_NAME
            )
    ).assertMessageIs("Illegal choiceId");
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_adminKey_is_rejected() {

    assertThrows(
        () -> choiceService
            .modifyChoice(
                voteData.adminInfoWithInvalidAdminKey,
                voteData.choice.getId(), NEW_CHOICE_NAME
            )
    ).assertMessageIs(BAD_ADMINKEY);
  }

  @TestedBehaviour("modifies the string of the choice")
  @Test
  public void proper_voteId_choiceId_and_adminKey_does_modify_choice() {

    choiceService
        .modifyChoice(
            voteData.adminInfoWithNoNeededAssurances, voteData.choice.getId(),
            NEW_CHOICE_NAME
        );

    assertEquals(NEW_CHOICE_NAME, voteData.choice.getName());
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void when_vote_is_not_modifiable_choices_cannot_be_modified() {

    assertThrows(
        () -> choiceService
            .modifyChoice(
                voteData.adminInfoUnmodifiable,
                voteData.choice.getId(),
                "something else"
            )
    )
        .assertMessageIs(UNMODIFIABLE);
  }

  @TestedBehaviour(
    "if 'user' is used as adminKey, then the user must be the one who added the choice and canAddIn be true"
  )
  @Test
  public void
      userAdmin_cannot_modify_choice_if_it_is_not_added_by_other_user() {
    voteData.voteWithNoNeededAssurances.getParameters().setAddinable(true);

    assertThrows(
        () -> choiceService
            .modifyChoice(
                voteData.adminInfoWithNoNeededAssurancesWithUser,
                voteData.choice.getId(),
                NEW_CHOICE_NAME
            )
    )
        .assertMessageIs(
            "Choice modification disallowed: adminKey is user, " +
                "and the choice was added by a different user"
        );

  }

  @TestedBehaviour(
    "if 'user' is used as adminKey, then the user must be the one who added the choice and canAddIn be true"
  )
  @Test
  public void
      userAdmin_can_modify_the_choice_if_canAddin_is_true_and_he_is_the_choice_creator() {
    voteData.voteWithNoNeededAssurances.getParameters().setAddinable(true);
    choiceService.modifyChoice(
        voteData.adminInfoWithNoNeededAssurancesWithUser,
        voteData.choiceOfAuthenticatedUser.getId(), NEW_CHOICE_NAME
    );

    assertEquals(NEW_CHOICE_NAME, voteData.choiceOfAuthenticatedUser.getName());
  }

}
