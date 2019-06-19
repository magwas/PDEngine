package org.rulez.demokracia.pdengine.showvote;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.rulez.demokracia.pdengine.authentication.AuthenticatedUserService;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;
import org.rulez.demokracia.pdengine.vote.VoteService;
import org.rulez.demokracia.pdengine.vote.VoteServiceContract;

public class ShowVoteTestBase extends ThrowableTester
    implements VoteServiceContract {

  @InjectMocks
  protected ShowVoteServiceImpl showVoteService;

  @Mock
  protected VoteService voteService;

  @Mock
  protected AuthenticatedUserService authService;

  protected VoteData voteData;

  public void setUp() {
    voteData = new VoteData();
    contract(voteService, voteData);

  }

}
