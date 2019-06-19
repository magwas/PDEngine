package org.rulez.demokracia.pdengine.choice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;

@TestedFeature("Manage votes")
@TestedOperation("delete choice")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ChoiceDeleteValidationTest extends ChoiceTestBase {

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void invalid_voteId_is_rejected() {
    assertThrows(
        () -> choiceService
            .deleteChoice(voteData.adminInfoWithInvalidVoteId, "choiceId")
    ).assertMessageIs(ILLEGAL_VOTE_ID);
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void invalid_choiceId_is_rejected() {
    final String invalidChoiceId = "InvalidChoiceId";
    assertThrows(
        () -> choiceService
            .deleteChoice(
                voteData.adminInfoWithNoNeededAssurances, invalidChoiceId
            )
    )
        .assertMessageIs("Illegal choiceId");
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void unmodifiable_vote_is_rejected() {
    final Choice choice = new Choice("ChoiceName", "user");
    voteData.voteWithNoNeededAssurances.addChoice(choice);

    assertThrows(
        () -> choiceService
            .deleteChoice(
                voteData.adminInfoUnmodifiable,
                choice.getId()
            )
    ).assertMessageIs(UNMODIFIABLE);
  }
}
