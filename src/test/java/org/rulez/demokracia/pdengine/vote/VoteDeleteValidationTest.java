package org.rulez.demokracia.pdengine.vote;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;

@TestedFeature("Manage votes")
@TestedOperation("delete vote")
@RunWith(MockitoJUnitRunner.Silent.class)
public class VoteDeleteValidationTest extends ThrowableTester
    implements VoteRepositoryContract, AdminKeyCheckerServiceContract {

  @InjectMocks
  private VoteServiceImpl voteService;

  @Mock
  private VoteRepository voteRepository;

  @Mock
  private AdminKeyCheckerService adminKeyCheckerService;

  private VoteData voteData;

  @Before
  public void setUp() {
    voteData = new VoteData();
    contract(voteRepository, voteData);
    contract(adminKeyCheckerService, voteData);
  }

  @TestedBehaviour("validate inputs")
  @Test
  public void invalid_voteId_is_rejected() {
    assertThrows(
        () -> voteService
            .deleteVote(new VoteAdminInfo(VoteData.invalidvoteId, "ADMIN"))
    )
        .assertMessageIs("illegal voteId");
  }

  @TestedBehaviour("validate inputs")
  @Test
  public void invalid_adminKey_is_rejected() {
    assertThrows(
        () -> voteService.deleteVote(voteData.adminInfoWithInvalidAdminKey)
    ).assertMessageIs(ILLEGAL_KEY);
  }

  @TestedBehaviour(
    "deletes the vote with all parameters, choices, ballots and votes cast"
  )
  @Test
  public void
      unmodifiable_vote_cannot_be_deleted_even_with_proper_voteId_and_adminKey() {
    assertThrows(
        () -> voteService.deleteVote(voteData.adminInfoUnmodifiable)
    )
        .assertMessageIs("This vote cannot be modified it has issued ballots.");
  }

  @TestedBehaviour(
    "deletes the vote with all parameters, choices, ballots and votes cast"
  )
  @Test
  public void proper_voteId_and_adminKey_does_delete_vote() {

    voteService
        .deleteVote(voteData.adminInfoForVoteWithOneNeededAssuranceWeHave);
    verify(voteRepository).delete(voteData.voteWithOneNeededAssuranceWeHave);
  }
}
