package org.rulez.demokracia.pdengine.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.RandomUtils;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.exception.ReportedException;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;

@TestedFeature("Manage votes")
@TestedOperation("modify vote")
@RunWith(MockitoJUnitRunner.class)
public class VoteModificationValidationTest extends ThrowableTester {

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

  private Vote existingVote;

  @Before
  public void setUp() {
    existingVote = new Vote("name", Set.of(), Set.of(), false, 1);
    when(voteRepository.findById(existingVote.getId()))
        .thenReturn(Optional.of(existingVote));
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void
      the_name_is_verified_against_the_same_rules_as_in_vote_creation() {
    final String modifiedVoteName = null;
    assertThrows(
        () -> voteService.modifyVote(
            new VoteAdminInfo(existingVote.getId(), existingVote.getAdminKey()), modifiedVoteName
        )
    )
        .assertMessageIs("vote name is null");
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_adminKey_is_rejected() {
    final String invalidAdminKey = RandomUtils.createRandomKey();
    doThrow(new ReportedException("IllegalKey")).when(adminKeyCheckerService)
        .checkAdminKey(existingVote, invalidAdminKey);

    assertThrows(
        () -> voteService
            .modifyVote(new VoteAdminInfo(existingVote.getId(), invalidAdminKey), NEW_VOTE_NAME)
    )
        .assertMessageIs("IllegalKey");
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_voteId_is_rejected() {
    final String invalidvoteId = RandomUtils.createRandomKey();
    assertThrows(
        () -> voteService
            .modifyVote(
                new VoteAdminInfo(invalidvoteId, existingVote.getAdminKey()), NEW_VOTE_NAME
            )
    )
        .assertMessageIs("illegal voteId");
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void valid_vote_should_be_renamed() {
    voteService.modifyVote(
        new VoteAdminInfo(existingVote.getId(), existingVote.getAdminKey()),
        NEW_VOTE_NAME
    );
    assertEquals(NEW_VOTE_NAME, existingVote.getName());
  }

  @TestedBehaviour(THE_VOTE_CANNOT_BE_MODIFIED_IF_THERE_ARE_BALLOTS_ISSUED)
  @Test
  public void modifyVote_with_ballot_get_an_exception() {
    existingVote.getBallots().add("TestBallots");

    assertThrows(
        () -> voteService.modifyVote(
            new VoteAdminInfo(existingVote.getId(), existingVote.getAdminKey()), NEW_VOTE_NAME
        )
    )
        .assertMessageIs("This vote cannot be modified it has issued ballots.");
  }
}
