package org.rulez.demokracia.pdengine.votecalculator;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.rulez.demokracia.pdengine.beattable.BeatTableService;
import org.rulez.demokracia.pdengine.beattable.BeatTableTransitiveClosureService;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.tally.Tally;
import org.rulez.demokracia.pdengine.tally.TallyService;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;
import org.rulez.demokracia.pdengine.testhelpers.VoteResultTestHelper;

public class ComputedVoteTestBase {

  @InjectMocks
  protected ComputedVoteServiceImpl computedVoteService;

  @Mock
  protected BeatTableService beatTableService;

  @Mock
  protected BeatTableTransitiveClosureService beatTableTransitiveClosureService;

  @Mock
  protected VoteResultComposer voteResultComposer;

  @Mock
  protected TallyService tallyService;

  protected ComputedVote computedVote;

  protected BeatTableData beatTableData;

  protected VoteData voteData;

  @Before
  public void setUp() {
    beatTableData = new BeatTableData();
    voteData = new VoteData();
    when(beatTableService.initializeBeatTable(any()))
        .thenReturn(beatTableData.beatTableComplex);
    when(beatTableService.normalize(any()))
        .thenReturn(beatTableData.beatTableTransitiveClosed);
    when(beatTableTransitiveClosureService.computeTransitiveClosure(any()))
        .thenReturn(beatTableData.beatTableTransitiveClosed);
    when(voteResultComposer.composeResult(any()))
        .thenReturn(VoteResultTestHelper.createVoteResults());
    when(tallyService.calculateTally(anyString(), any()))
        .then(a -> new Tally((String) a.getArgument(0)));

    computedVote = computedVoteService
        .computeVote(voteData.voteWithTwoCountedAssurancesWeDontHaveOne);
  }
}
