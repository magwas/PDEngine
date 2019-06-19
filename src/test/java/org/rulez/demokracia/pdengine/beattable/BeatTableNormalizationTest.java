package org.rulez.demokracia.pdengine.beattable;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.beattable.BeatTable.Direction;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;

@TestedFeature("Schulze method")
@TestedOperation("normalize beat matrix")
@RunWith(MockitoJUnitRunner.Silent.class)
public class BeatTableNormalizationTest extends ThrowableTester {

  @InjectMocks
  private BeatTableServiceImpl beatTableService;

  private BeatTable normalizedBeatTable;

  private BeatTableData beatTableData;

  private BeatTable normalizedBeatTableTied;

  @Before
  public void setUp() {
    beatTableData = new BeatTableData();
    normalizedBeatTable =
        beatTableService.normalize(beatTableData.beatTable);
    normalizedBeatTableTied =
        beatTableService.normalize(beatTableData.beatTableTied);

  }

  @TestedBehaviour("the diagonal elements are (0,0)")
  @Test
  public void normalization_sets_the_diagonal_to_0_0() {
    assertAllDiagonalElementsAreZero();
  }

  private void assertAllDiagonalElementsAreZero() {
    normalizedBeatTable.getKeyCollection()
        .forEach(k -> assertDiagonalElementIsZero(k));
  }

  @TestedBehaviour("the elements corresponding to loosers are (0,0)")
  @Test
  public void normalization_sets_the_looser_to_0_0() {
    final Pair element =
        normalizedBeatTable.getElement(VoteData.CHOICE2, VoteData.CHOICE1);
    assertEquals(new Pair(0, 0), element);
  }

  @TestedBehaviour(
    "the elements corresponding to winners contain the number of looses backward"
  )
  @Test
  public void normalization_does_not_modify_the_winners_number_of_looses() {
    final int actualResult =
        normalizedBeatTable
            .beatInformation(
                VoteData.CHOICE1, VoteData.CHOICE2, Direction.DIRECTION_BACKWARD
            );
    assertEquals(
        BeatTableData.PAIR_OF_CHOICE1_CHOICE2.getLosing(), actualResult
    );
  }

  @TestedBehaviour(
    "the elements corresponding to winners contain the number of wins forward"
  )
  @Test
  public void normalization_does_not_modify_the_winners_number_of_wins() {
    final int actualResult =
        normalizedBeatTable
            .beatInformation(
                VoteData.CHOICE1, VoteData.CHOICE2, Direction.DIRECTION_FORWARD
            );
    assertEquals(
        BeatTableData.PAIR_OF_CHOICE1_CHOICE2.getWinning(), actualResult
    );
  }

  @TestedBehaviour("the elements for ties are (0,0)")
  @Test
  public void normalization_set_the_ties_to_0_0() {
    final Pair element =
        normalizedBeatTableTied.getElement(VoteData.CHOICE3, VoteData.CHOICE1);
    assertEquals(new Pair(0, 0), element);
  }

  @TestedBehaviour("the elements for ties are (0,0)")
  @Test
  public void normalization_set_the_other_part_of_the_ties_to_0_0_too() {
    final Pair element =
        normalizedBeatTableTied.getElement(VoteData.CHOICE1, VoteData.CHOICE3);
    assertEquals(new Pair(0, 0), element);
  }

  @TestedBehaviour("the elements for ties are (0,0)")
  @Test
  public void
      normalization_does_not_modify_the_values_when_the_selected_beats_are_not_ties() {
    final Pair element =
        normalizedBeatTableTied.getElement(VoteData.CHOICE1, VoteData.CHOICE2);
    assertEquals(BeatTableData.PAIR_OF_CHOICE1_CHOICE2, element);
  }

  private void assertDiagonalElementIsZero(final String key) {
    final Pair element = normalizedBeatTable.getElement(key, key);
    assertEquals(new Pair(0, 0), element);
  }
}
