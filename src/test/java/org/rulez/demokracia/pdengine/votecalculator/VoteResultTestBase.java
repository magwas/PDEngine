package org.rulez.demokracia.pdengine.votecalculator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;

public class VoteResultTestBase {

  @InjectMocks
  protected VoteResultComposerImpl voteResultComposer;
  @Mock
  protected WinnerCalculatorService winnerCalculatorService;

  protected Set<String> choicesReturned;
  protected List<VoteResult> result;
  protected BeatTableData beatTableData;
  final List<List<String>> expectedWinners =
      List.of(
          List.of(VoteData.CHOICE1, VoteData.CHOICE5),
          List.of(VoteData.CHOICE4),
          List.of(VoteData.CHOICE2),
          List.of(VoteData.CHOICE3)
      );

  @Before
  public void setUp() {
    beatTableData = new BeatTableData();
    when(
        winnerCalculatorService.calculateWinners(any(), any())
    )
        .thenReturn(List.of(VoteData.CHOICE1, VoteData.CHOICE5))
        .thenReturn(List.of(VoteData.CHOICE4))
        .thenReturn(List.of(VoteData.CHOICE2))
        .thenReturn(List.of(VoteData.CHOICE3));

    result = voteResultComposer
        .composeResult(beatTableData.beatTableTransitiveClosed);
    choicesReturned = convertResultToChoiceSet(result);
  }

  private Set<String> convertResultToChoiceSet(final List<VoteResult> result) {
    return result.stream()
        .map(VoteResult::getWinners)
        .flatMap(List::stream)
        .collect(Collectors.toSet());
  }
}
