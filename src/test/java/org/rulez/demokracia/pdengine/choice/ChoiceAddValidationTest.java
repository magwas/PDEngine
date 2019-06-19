package org.rulez.demokracia.pdengine.choice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;

@TestedFeature("Manage votes")
@TestedOperation("Add choice")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ChoiceAddValidationTest extends ChoiceTestBase {

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void invalid_vote_admin_info_is_rejected() {

    assertaddChoiceThrowsUp(voteData.adminInfoWithInvalidVoteId)
        .assertMessageIs(ILLEGAL_VOTE_ID);
  }

  @TestedBehaviour(
    "No choice can be added if the vote is not modifiable."
  )
  @Test
  public void no_choice_can_be_added_if_the_vote_is_not_modifiable() {
    assertaddChoiceThrowsUp(
        voteData.adminInfoUnmodifiable
    )
        .assertMessageIs(UNMODIFIABLE);
  }

  private ThrowableTester
      assertaddChoiceThrowsUp(final VoteAdminInfo adminInfo) {
    return assertThrows(() -> {
      choiceService
          .addChoice(adminInfo, "choice1", "user");
    });
  }
}
