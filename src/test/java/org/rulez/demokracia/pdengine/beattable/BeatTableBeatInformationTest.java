package org.rulez.demokracia.pdengine.beattable;

import static org.junit.Assert.assertEquals;
import static org.rulez.demokracia.pdengine.testhelpers.BeatTableData.*;

import org.junit.Before;
import org.junit.Test;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.beattable.BeatTable.Direction;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;

@TestedFeature("Supporting functionality")
@TestedOperation("BeatTable")
@TestedBehaviour(
  "the beat information related to a and b can be obtained for forward and backward"
)
public class BeatTableBeatInformationTest extends ThrowableTester {

  private BeatTableData beatTableData;

  @Before
  public void setUp() {
    beatTableData = new BeatTableData();
  }

  @Test
  public void
      beatInformation_throws_an_exception_when_direction_is_not_defined() {
    assertThrows(
        () -> beatTableData.beatTable.beatInformation(null, null, null)
    )
        .assertMessageIs(ContainingBeats.INVALID_DIRECTION);
  }

  @Test
  public void
      beatInformation_gives_back_the_number_of_winnings_from_choice1_to_choice2() {

    assertEquals(
        PAIR_OF_CHOICE1_CHOICE2.getWinning(),
        beatTableData.beatTable
            .beatInformation(
                VoteData.CHOICE1, VoteData.CHOICE2, Direction.DIRECTION_FORWARD
            )
    );
  }

  @Test
  public void
      beatInformation_gives_back_the_number_of_losing_from_choice1_to_choice2() {

    assertEquals(
        PAIR_OF_CHOICE1_CHOICE2.getLosing(),
        beatTableData.beatTable
            .beatInformation(
                VoteData.CHOICE1, VoteData.CHOICE2, Direction.DIRECTION_BACKWARD
            )
    );
  }

  @Test
  public void
      beatInformation_throws_exception_when_getting_with_invalid_row_key() {
    assertThrows(
        () -> beatTableData.beatTable
            .getElement(VoteData.CHOICE1, INVALID_ROW)
    )
        .assertMessageIs(MapMatrix.INVALID_ROW_KEY);
  }

  @Test
  public void
      beatInformation_throws_exception_when_getting_with_invalid_column_key() {
    assertThrows(
        () -> beatTableData.beatTable
            .getElement(INVALID_COLUMN, VoteData.CHOICE1)
    )
        .assertMessageIs(MapMatrix.INVALID_COLUMN_KEY);
  }

  @Test
  public void
      beatInformation_throws_exception_when_setting_with_invalid_row_key() {
    assertThrows(
        () -> beatTableData.beatTable
            .setElement(VoteData.CHOICE1, INVALID_ROW, new Pair(0, 0))
    )
        .assertMessageIs(MapMatrix.INVALID_ROW_KEY);
  }

  @Test
  public void
      beatInformation_throws_exception_when_setting_with_invalid_column_key() {
    assertThrows(
        () -> beatTableData.beatTable
            .setElement(INVALID_COLUMN, VoteData.CHOICE1, new Pair(0, 0))
    )
        .assertMessageIs(MapMatrix.INVALID_COLUMN_KEY);
  }
}
