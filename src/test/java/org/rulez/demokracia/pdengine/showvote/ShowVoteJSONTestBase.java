package org.rulez.demokracia.pdengine.showvote;

import org.junit.Before;
import org.rulez.demokracia.pdengine.vote.Vote;

import com.google.gson.JsonObject;

public class ShowVoteJSONTestBase extends ShowVoteTestBase {

  protected JsonObject result;
  protected Vote vote;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    result = createJson();
    vote = voteData.voteWithOneCountedAssuranceWeHave;
  }

  protected JsonObject createJson() {
    return showVoteService
        .showVote(voteData.adminInfoForVoteWithOneCountedAssuranceWeHave);
  }
}
