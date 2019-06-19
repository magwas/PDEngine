package org.rulez.demokracia.pdengine.vote;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;

@TestedFeature("Manage votes")
@TestedOperation("create vote")
@TestedBehaviour("Creates a vote")
@RunWith(MockitoJUnitRunner.Silent.class)
public class VoteServiceQueryTest extends ThrowableTester
    implements VoteRepositoryContract {

  @InjectMocks
  private VoteServiceImpl voteService;
  @Mock
  private VoteRepository voteRepository;

  private VoteData voteData;

  @Before
  public void setUp() {
    voteData = new VoteData();
    contract(voteRepository, voteData);
  }

  @Test
  public void get_vote_returns_the_vote_from_repository() {
    assertEquals(
        voteData.voteWithNoNeededAssurances,
        voteService.getVote(voteData.voteWithNoNeededAssurances.getId())
    );
  }

  @Test
  public void get_vote_throws_exception_when_vote_not_found() {
    assertThrows(
        () -> voteService.getVote(VoteData.invalidvoteId)
    )
        .assertMessageIs(VoteService.ILLEGAL_VOTE_ID);
  }
}
