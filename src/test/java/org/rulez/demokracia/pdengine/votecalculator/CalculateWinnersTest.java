package org.rulez.demokracia.pdengine.votecalculator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
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
import org.rulez.demokracia.pdengine.beattable.BeatTable;
import org.rulez.demokracia.pdengine.beattable.BeatTableIgnoreService;
import org.rulez.demokracia.pdengine.beattable.Pair;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;

@TestedFeature("Vote")
@TestedOperation("calculate winners")
@RunWith(MockitoJUnitRunner.Silent.class)
public class CalculateWinnersTest {

  @InjectMocks
  private WinnerCalculatorServiceImpl winnerCalculator;

  @Mock
  private BeatTableIgnoreService beatTableIgnore;

  private BeatTableData beatTableData;

  @Before
  public void setUp() {
    beatTableData = new BeatTableData();

    when(
        beatTableIgnore
            .ignoreChoices(
                beatTableData.beatTableTransitiveClosed,
                List.of(VoteData.CHOICE1)
            )
    )
        .thenReturn(
            beatTableData.beatTableFiltered
        );
    when(
        beatTableIgnore
            .ignoreChoices(beatTableData.beatTableTransitiveClosed, Set.of())
    )
        .thenReturn(beatTableData.beatTableTransitiveClosed);
  }

  @TestedBehaviour("only choices not in ignoredChoices are considered")
  @Test
  public void calculate_winners_returns_none_of_the_ignored_choices() {
    final Collection<String> ignoredChoices = List.of(VoteData.CHOICE1);
    final List<String> winners =
        winnerCalculator.calculateWinners(
            beatTableData.beatTableTransitiveClosed, ignoredChoices
        );
    assertFalse(winners.contains(VoteData.CHOICE1));

  }

  @TestedBehaviour("only choices not in ignoredChoices are considered")
  @Test
  public void calculate_winners_returns_not_ignored_winner() {
    final List<String> winners =
        winnerCalculator.calculateWinners(
            beatTableData.beatTableTransitiveClosed,
            List.of(VoteData.CHOICE1)
        );
    assertFalse(winners.contains(VoteData.CHOICE1));
  }

  @TestedBehaviour("all non-beaten candidates are winners")
  @Test
  public void calculate_winners_doesnt_return_beaten_candidates() {
    assertNoWinnerIsBeaten(
        winnerCalculator
            .calculateWinners(beatTableData.beatTableTransitiveClosed, Set.of())
    );
  }

  @TestedBehaviour("all non-beaten candidates are winners")
  @Test
  public void calculate_winners_return_all_non_beaten_candidates() {
    final List<String> winners = winnerCalculator
        .calculateWinners(beatTableData.beatTableTransitiveClosed, Set.of());
    assertNonbeatensAreWinner(
        winners
    );
  }

  private void assertNoWinnerIsBeaten(final List<String> winners) {
    assertTrue(
        winners.stream().allMatch(
            choice -> isAWinner(choice, beatTableData.beatTableTransitiveClosed)
        )
    );
  }

  private void assertNonbeatensAreWinner(final List<String> winners) {
    assertTrue(
        beatTableData.beatTableTransitiveClosed.getKeyCollection().stream()
            .filter(
                choice -> isAWinner(
                    choice, beatTableData.beatTableTransitiveClosed
                )
            )
            .allMatch(choice -> winners.contains(choice))
    );
  }

  private boolean
      isAWinner(final String player1, final BeatTable beatPathTable) {
    for (final String player2 : beatPathTable.getKeyCollection()) {
      final Pair forward = beatPathTable.getElement(player2, player1);
      final Pair backward = beatPathTable.getElement(player1, player2);
      if (!forward.equals(beatPathTable.compareBeats(forward, backward)))
        return false;
    }
    return true;
  }
}
