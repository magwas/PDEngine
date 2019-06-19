package org.rulez.demokracia.pdengine.choice;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.rulez.demokracia.pdengine.Context;
import org.rulez.demokracia.pdengine.authentication.AuthenticatedUserService;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;
import org.rulez.demokracia.pdengine.vote.AdminKeyCheckerService;
import org.rulez.demokracia.pdengine.vote.AdminKeyCheckerServiceContract;
import org.rulez.demokracia.pdengine.vote.VoteService;
import org.rulez.demokracia.pdengine.vote.VoteServiceContract;

public class ChoiceTestBase extends ThrowableTester
    implements VoteServiceContract, AdminKeyCheckerServiceContract {

  @InjectMocks
  protected ChoiceServiceImpl choiceService;

  @Mock
  protected VoteService voteService;
  @Mock
  protected AuthenticatedUserService authService;

  @Mock
  protected AdminKeyCheckerService adminKeyCheckerService;

  protected VoteData voteData;

  private Context context;

  @Before
  public void setUp() {
    voteData = new VoteData();
    contract(voteService, voteData);
    context = new Context();
    contract(authService, context);
    contract(adminKeyCheckerService, voteData);

  }

}
