package org.rulez.demokracia.pdengine.showvote;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;

import com.google.gson.JsonObject;

@TestedFeature("Manage votes")
@TestedOperation("show vote")
@TestedBehaviour(
  "if adminKey is anon, the user should have any of the countedAssurances"
)
@RunWith(MockitoJUnitRunner.Silent.class)
public class ForShowVoteTheUserNeedsACountedAssuranceTest
    extends ShowVoteTestBase {

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Test
  public void a_user_with_not_all_assourances_cannot_show_the_vote() {
    assertAssurancesMissing(
        voteData.adminInfoForVoteWeDontHaveCountedAssurance
    );
  }

  @Test
  public void
      a_user_with_not_all_assourances_cannot_show_the_vote_even_with_more_assurances() {
    assertAssurancesMissing(
        voteData.adminInfoForVoteWeDontHaveOneCountedAssurance
    );
  }

  @Test
  public void a_user_with_all_assourances_can_show_the_vote() {
    final JsonObject voteJson =
        showVoteService
            .showVote(voteData.adminInfoForVoteWithOneCountedAssuranceWeHave);

    assertNotNull(voteJson);
  }

  private void assertAssurancesMissing(final VoteAdminInfo adminInfo) {
    assertThrows(
        () -> showVoteService
            .showVote(adminInfo)
    )
        .assertMessageIs(MISSING_ASSURANCES);
  }

}
