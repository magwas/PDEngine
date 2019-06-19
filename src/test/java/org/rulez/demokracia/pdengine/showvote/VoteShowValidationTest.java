package org.rulez.demokracia.pdengine.showvote;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;

@TestedFeature("Manage votes")
@TestedOperation("show vote")
@TestedBehaviour("validates inputs")
@RunWith(MockitoJUnitRunner.Silent.class)
public class VoteShowValidationTest extends ShowVoteTestBase {

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Test
  public void invalid_voteId_is_rejected() {
    assertThrows(
        () -> showVoteService.showVote(voteData.adminInfoWithInvalidVoteId)
    )
        .assertMessageIs(ILLEGAL_VOTE_ID);
  }

  @Test
  public void invalid_adminKey_is_rejected() {
    assertThrows(
        () -> showVoteService.showVote(voteData.adminInfoWithInvalidAdminKey)
    )
        .assertMessageIs(BAD_ADMINKEY);
  }
}
