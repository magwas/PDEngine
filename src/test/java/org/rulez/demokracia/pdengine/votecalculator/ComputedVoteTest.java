package org.rulez.demokracia.pdengine.votecalculator;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.authentication.AuthServiceContract;
import org.rulez.demokracia.pdengine.beattable.BeatTable;
import org.rulez.demokracia.pdengine.beattable.BeatTableService;
import org.rulez.demokracia.pdengine.beattable.BeatTableTransitiveClosureService;
import org.rulez.demokracia.pdengine.dataobjects.CastVoteData;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.tally.Tally;
import org.rulez.demokracia.pdengine.tally.TallyService;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;
import org.rulez.demokracia.pdengine.testhelpers.VoteResultTestHelper;

@TestedFeature("Vote")
@TestedOperation("Compute vote results")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ComputedVoteTest {

  @InjectMocks
  private ComputedVoteServiceImpl computedVoteService;

  @Mock
  private BeatTableService beatTableService;

  @Mock
  private BeatTableTransitiveClosureService beatTableTransitiveClosureService;

  @Mock
  private VoteResultComposer voteResultComposer;

  @Mock
  TallyService tallyService;

  private ComputedVote computedVote;

  private BeatTableData beatTableData;

  private CastVoteData castVoteData;

  private VoteData voteData;

  @Before
  public void setUp() {
    voteData = new VoteData();
    castVoteData = voteData.castVoteData;
    beatTableData = voteData.beatTableData;
    when(beatTableService.initializeBeatTable(castVoteData.castVoteList))
        .thenReturn(beatTableData.beatTableComplex);
    when(beatTableService.normalize(beatTableData.beatTableComplex))
        .thenReturn(beatTableData.beatTableNormalized);
    when(
        beatTableTransitiveClosureService
            .computeTransitiveClosure(beatTableData.beatTableComplex)
    )
        .thenReturn(beatTableData.beatTableTransitiveClosed);
    when(voteResultComposer.composeResult(any()))
        .thenReturn(VoteResultTestHelper.createVoteResults());

    doReturn(new Tally(AuthServiceContract.ASSURANCE_WE_HAVE))
        .when(tallyService)
        .calculateTally(
            AuthServiceContract.ASSURANCE_WE_HAVE, castVoteData.castVoteList
        );

    doReturn(new Tally(AuthServiceContract.ASSURANCE_WE_DONT_HAVE))
        .when(tallyService)
        .calculateTally(
            AuthServiceContract.ASSURANCE_WE_DONT_HAVE,
            castVoteData.castVoteList
        );
    when(beatTableService.initializeBeatTable(castVoteData.castVoteList))
        .thenReturn(beatTableData.beatTableComplex);
    when(beatTableService.normalize(beatTableData.beatTableComplex))
        .thenReturn(beatTableData.beatTableNormalized);
    doReturn(beatTableData.beatTableTransitiveClosed)
        .when(beatTableTransitiveClosureService)
        .computeTransitiveClosure(
            voteData.beatTableData.beatTableNormalized
        );

    computedVote = computedVoteService
        .computeVote(voteData.voteWithTwoCountedAssurancesWeDontHaveOne);
  }

  @TestedBehaviour("compares and stores initial beat matrix")
  @Test
  public void compute_vote_should_create_initial_matrix_with_full_key_set() {
    assertEquals(
        Set.copyOf(BeatTableData.allChoices),
        Set.copyOf(computedVote.getBeatTable().getKeyCollection())
    );
  }

  @TestedBehaviour("compares and stores initial beat matrix")
  @Test
  public void after_compute_vote_beat_table_should_contain_beat_information() {
    assertBeatTableEquals(
        computedVote.getBeatTable(), beatTableData.beatTableComplex
    );
  }

  @TestedBehaviour("calculates and stores beatpath matrix")
  @Test
  public void beat_path_matrix_is_not_the_same_as_initial_matrix() {
    assertNotSame(computedVote.getBeatTable(), computedVote.getBeatPathTable());
  }

  @TestedBehaviour("calculates and stores beatpath matrix")
  @Test
  public void beat_path_matrix_is_normalized() {
    verify(beatTableService).normalize(computedVote.getBeatTable());
  }

  @TestedBehaviour("calculates and stores beatpath matrix")
  @Test
  public void transitive_closure_done_on_beat_path_matrix() {

    assertBeatTableEquals(
        beatTableData.beatTableTransitiveClosed, computedVote.getBeatPathTable()
    );
  }

  @TestedBehaviour("calculates and stores vote results")
  @Test
  public void vote_results_stored_in_computed_vote() {
    assertFalse(computedVote.getVoteResults().isEmpty());
  }

  private void assertBeatTableEquals(
      final BeatTable firstBeatTable, final BeatTable secondBeatTable
  ) {
    for (final String choice1 : firstBeatTable.getKeyCollection())
      for (final String choice2 : firstBeatTable.getKeyCollection())
        assertEquals(
            secondBeatTable.getElement(choice1, choice2), firstBeatTable.getElement(choice1, choice2)
        );
  }

  @TestedBehaviour(
    "vote result includes the votes cast with the secret cast vote identifier."
  )
  @Test
  public void
      vote_result_includes_the_votes_cast_with_the_secret_cast_vote_id() {
    final String secretId =
        computedVote.getVote().getVotesCast().get(0).getSecretId();
    assertFalse(secretId.isEmpty());
  }
}
