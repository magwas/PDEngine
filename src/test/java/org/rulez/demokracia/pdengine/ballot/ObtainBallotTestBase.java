package org.rulez.demokracia.pdengine.ballot;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.rulez.demokracia.pdengine.Context;
import org.rulez.demokracia.pdengine.authentication.AuthenticatedUserService;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;
import org.rulez.demokracia.pdengine.vote.AdminKeyCheckerService;
import org.rulez.demokracia.pdengine.vote.AdminKeyCheckerServiceContract;

public class ObtainBallotTestBase extends ThrowableTester
    implements AdminKeyCheckerServiceContract {

  @InjectMocks
  protected BallotServiceImpl ballotService;

  @Mock
  protected AuthenticatedUserService authService;

  @Mock
  protected AdminKeyCheckerService adminKeyCheckerService;

  protected VoteData voteData;

  protected Context context;

  @Before
  public void setUp() {
    voteData = new VoteData();
    context = new Context();
    contract(authService, context);
    contract(adminKeyCheckerService, voteData);
  }

}
