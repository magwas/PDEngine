package org.rulez.demokracia.pdengine.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
@TestedOperation("modify vote")
@RunWith(MockitoJUnitRunner.Silent.class)
public class VoteModificationValidationTest extends ThrowableTester
    implements VoteRepositoryContract, AdminKeyCheckerServiceContract {

  private static final String THE_VOTE_CANNOT_BE_MODIFIED_IF_THERE_ARE_BALLOTS_ISSUED =
      "The vote cannot be modified if there are ballots issued.";

  private static final String VALIDATES_INPUTS = "validates inputs";

  private static final String NEW_VOTE_NAME = "newVoteName";

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
    this.contract(adminKeyCheckerService, voteData);
    this.contract(voteRepository, voteData);
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void
      the_name_is_verified_against_the_same_rules_as_in_vote_creation() {
    final String modifiedVoteName = null;
    assertThrows(
        () -> voteService.modifyVote(
            voteData.adminInfoWithNoNeededAssurances,
            modifiedVoteName
        )
    )
        .assertMessageIs("vote name is null");
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_adminKey_is_rejected() {

    assertThrows(
        () -> voteService
            .modifyVote(voteData.adminInfoWithInvalidAdminKey, NEW_VOTE_NAME)
    )
        .assertMessageIs(ILLEGAL_KEY);
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_voteId_is_rejected() {
    assertThrows(
        () -> voteService
            .modifyVote(
                voteData.adminInfoWithInvalidVoteId, NEW_VOTE_NAME
            )
    )
        .assertMessageIs(VoteService.ILLEGAL_VOTE_ID);
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void valid_vote_should_be_renamed() {
    voteService.modifyVote(
        voteData.adminInfoWithNoNeededAssurances,
        NEW_VOTE_NAME
    );
    assertEquals(NEW_VOTE_NAME, voteData.voteWithNoNeededAssurances.getName());
  }

  @TestedBehaviour(THE_VOTE_CANNOT_BE_MODIFIED_IF_THERE_ARE_BALLOTS_ISSUED)
  @Test
  public void modifyVote_with_ballot_get_an_exception() {
    assertThrows(
        () -> voteService.modifyVote(
            voteData.adminInfoUnmodifiable, NEW_VOTE_NAME
        )
    )
        .assertMessageIs("This vote cannot be modified it has issued ballots.");
  }
}
