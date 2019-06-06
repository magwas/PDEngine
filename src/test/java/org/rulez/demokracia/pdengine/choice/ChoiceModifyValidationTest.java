package org.rulez.demokracia.pdengine.choice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.exception.ReportedException;

@TestedFeature("Manage votes")
@TestedOperation("modify vote")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ChoiceModifyValidationTest extends ChoiceTestBase {

  private static final String VALIDATES_INPUTS = "validates inputs";
  private static final String NEW_CHOICE_NAME = "NewChoiceName";
  private static final String USER = "user";
  private static final String ADMIN = "admin";

  private final Choice choice = new Choice("choiceName", USER);
  private VoteAdminInfo adminInfo;
  private VoteAdminInfo adminInfoWithUser;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    vote.addChoice(choice);
    adminInfo = new VoteAdminInfo(vote.getId(), vote.getAdminKey());
    when(voteService.getModifiableVote(adminInfo)).thenReturn(vote);
    adminInfoWithUser = new VoteAdminInfo(vote.getId(), USER);
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_voteId_is_rejected() {
    final VoteAdminInfo adminInfo2 =
        new VoteAdminInfo(invalidvoteId, vote.getAdminKey());
    doThrow(new ReportedException("illegal voteId", invalidvoteId))
        .when(voteService)
        .getModifiableVote(adminInfo2);
    assertThrows(
        () -> choiceService
            .modifyChoice(
                adminInfo2,
                choice.getId(), NEW_CHOICE_NAME
            )
    ).assertMessageIs("illegal voteId");
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_choiceId_is_rejected() {
    final String invalidChoiceId = "invalidChoiceId";

    assertThrows(
        () -> choiceService
            .modifyChoice(adminInfo, invalidChoiceId, NEW_CHOICE_NAME)
    ).assertMessageIs("Illegal choiceId");
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_adminKey_is_rejected() {
    final String invalidAdminKey = "invalidAdminKey";

    final VoteAdminInfo adminInfo2 =
        new VoteAdminInfo(vote.getId(), invalidAdminKey);
    doThrow(new ReportedException("bad adminkey"))
        .when(voteService)
        .getModifiableVote(adminInfo2);

    assertThrows(
        () -> choiceService
            .modifyChoice(
                adminInfo2,
                choice.getId(), NEW_CHOICE_NAME
            )
    ).assertMessageIs("bad adminkey");
  }

  @TestedBehaviour("modifies the string of the choice")
  @Test
  public void proper_voteId_choiceId_and_adminKey_does_modify_choice() {

    choiceService.modifyChoice(adminInfo, choice.getId(), NEW_CHOICE_NAME);

    assertEquals(NEW_CHOICE_NAME, choice.getName());
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void when_vote_is_not_modifiable_choices_cannot_be_modified() {
    vote.getBallots().add("Test Ballot");
    doThrow(new ReportedException("unmodifiable", invalidvoteId))
        .when(voteService)
        .getModifiableVote(adminInfo);

    assertThrows(
        () -> choiceService
            .modifyChoice(adminInfo, choice.getId(), "something else")
    )
        .assertMessageIs("unmodifiable");
  }

  @TestedBehaviour(
    "if 'user' is used as adminKey, then the user must be the one who added the choice and canAddIn be true"
  )
  @Test
  public void choice_for_unmodifiable_vote_cannot_be_modified() {
    vote.getParameters().setAddinable(false);

    doThrow(new ReportedException("unmodifiable", invalidvoteId))
        .when(voteService)
        .getModifiableVote(adminInfo);

    assertThrows(
        () -> choiceService
            .modifyChoice(adminInfo, choice.getId(), NEW_CHOICE_NAME)
    ).assertMessageIs("unmodifiable");

  }

  @TestedBehaviour(
    "if 'user' is used as adminKey, then the user must be the one who added the choice and canAddIn be true"
  )
  @Test
  public void
      userAdmin_cannot_modify_choice_if_it_is_not_added_by_other_user() {
    vote.getParameters().setAddinable(true);

    doReturn(vote)
        .when(voteService)
        .getModifiableVote(adminInfoWithUser);

    assertThrows(
        () -> choiceService
            .modifyChoice(adminInfoWithUser, choice.getId(), NEW_CHOICE_NAME)
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
    vote.getParameters().setAddinable(true);
    final Choice choice2 = new Choice("choice2", ADMIN);
    vote.addChoice(choice2);
    when(authService.getAuthenticatedUserName()).thenReturn(ADMIN);
    doReturn(vote)
        .when(voteService)
        .getModifiableVote(adminInfoWithUser);
    choiceService.modifyChoice(
        adminInfoWithUser, choice2.getId(), NEW_CHOICE_NAME
    );

    assertEquals(NEW_CHOICE_NAME, choice2.getName());
  }

}
