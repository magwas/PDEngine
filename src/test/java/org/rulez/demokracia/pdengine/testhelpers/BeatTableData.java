package org.rulez.demokracia.pdengine.testhelpers;

import java.util.List;

import org.rulez.demokracia.pdengine.beattable.BeatTable;
import org.rulez.demokracia.pdengine.beattable.Pair;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;

public class BeatTableData {

  public static final String INVALID_COLUMN = "InvalidColumn";
  public static final String INVALID_ROW = "InvalidRow";

  private final static String CHOICE1 = VoteData.CHOICE1;
  private final static String CHOICE2 = VoteData.CHOICE2;
  private final static String CHOICE3 = VoteData.CHOICE3;
  private final static String CHOICE4 = VoteData.CHOICE4;
  private final static String CHOICE5 = VoteData.CHOICE5;

  public static final Pair PAIR_OF_CHOICE1_CHOICE2 = new Pair(6, 2);
  public static final List<String> twoChoices = List.of(CHOICE1, CHOICE2);
  public static final List<String> tiedChoices =
      List.of(CHOICE1, CHOICE3, CHOICE2);
  public static final List<String> allChoices =
      List.of(CHOICE1, CHOICE2, CHOICE3, CHOICE4, CHOICE5);

  public final BeatTable beatTable;
  public final BeatTable beatTableTied;
  public final BeatTable beatTableFiltered;
  public final BeatTable beatTableComplex;
  public final BeatTable beatTableTransitiveClosed;
  public BeatTable beatTableNormalized;

  public BeatTableData() {
    beatTable = createNewBeatTableWithComplexData(twoChoices);
    beatTableTied =
        createNewBeatTableWithComplexData(tiedChoices);
    beatTableComplex = createNewBeatTableWithComplexData(
        allChoices
    );
    beatTableNormalized = createBeatTableWithNormalizedData(allChoices);
    beatTableTransitiveClosed = createTransitiveClosedBeatTable(allChoices);
    beatTableFiltered =
        createTransitiveClosedBeatTable(List.of(CHOICE2, CHOICE3, CHOICE4));
  }

  private BeatTable
      createNewBeatTableWithComplexData(final List<String> choices) {
    final BeatTable beatTable = new BeatTable(choices);
    addPairIfInKeys(beatTable, CHOICE1, CHOICE2, new Pair(6, 2));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE1, new Pair(2, 6));

    addPairIfInKeys(beatTable, CHOICE1, CHOICE3, new Pair(4, 4));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE3, new Pair(5, 3));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE1, new Pair(4, 4));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE2, new Pair(3, 5));

    addPairIfInKeys(beatTable, CHOICE1, CHOICE4, new Pair(7, 1));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE4, new Pair(2, 6));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE4, new Pair(3, 5));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE1, new Pair(1, 7));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE2, new Pair(6, 2));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE3, new Pair(5, 3));

    addPairIfInKeys(beatTable, CHOICE1, CHOICE5, new Pair(1, 1));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE5, new Pair(1, 0));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE5, new Pair(1, 0));
    addPairIfInKeys(beatTable, CHOICE5, CHOICE1, new Pair(1, 1));
    addPairIfInKeys(beatTable, CHOICE5, CHOICE2, new Pair(0, 1));
    addPairIfInKeys(beatTable, CHOICE5, CHOICE3, new Pair(0, 1));
    return beatTable;
  }

  private BeatTable
      createBeatTableWithNormalizedData(final List<String> choices) {
    final BeatTable beatTable = new BeatTable(choices);
    addPairIfInKeys(beatTable, CHOICE1, CHOICE1, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE2, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE1, CHOICE2, new Pair(6, 2));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE1, new Pair(0, 0));

    addPairIfInKeys(beatTable, CHOICE3, CHOICE3, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE1, CHOICE3, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE3, new Pair(5, 3));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE1, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE2, new Pair(0, 0));

    addPairIfInKeys(beatTable, CHOICE4, CHOICE4, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE1, CHOICE4, new Pair(7, 1));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE4, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE4, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE1, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE2, new Pair(6, 2));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE3, new Pair(5, 3));

    addPairIfInKeys(beatTable, CHOICE2, CHOICE5, new Pair(1, 0));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE5, new Pair(1, 0));
    return beatTable;
  }

  private BeatTable
      createTransitiveClosedBeatTable(final List<String> choices) {
    final BeatTable beatTable = new BeatTable(choices);
    addPairIfInKeys(beatTable, CHOICE1, CHOICE1, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE2, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE1, CHOICE2, new Pair(6, 2));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE1, new Pair(0, 0));

    addPairIfInKeys(beatTable, CHOICE3, CHOICE3, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE1, CHOICE3, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE3, new Pair(5, 3));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE1, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE2, new Pair(0, 0));

    addPairIfInKeys(beatTable, CHOICE4, CHOICE4, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE1, CHOICE4, new Pair(7, 1));
    addPairIfInKeys(beatTable, CHOICE2, CHOICE4, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE3, CHOICE4, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE1, new Pair(0, 0));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE2, new Pair(6, 2));
    addPairIfInKeys(beatTable, CHOICE4, CHOICE3, new Pair(5, 3));
    return beatTable;
  }

  private static void addPairIfInKeys(
      final BeatTable beatTable, final String winner,
      final String loser, final Pair pair
  ) {
    if (
      beatTable.getKeyCollection().contains(winner) &&
          beatTable.getKeyCollection().contains(loser)
    )
      beatTable.setElement(winner, loser, pair);
  }
}
