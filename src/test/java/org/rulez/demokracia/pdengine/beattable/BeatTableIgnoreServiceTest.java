package org.rulez.demokracia.pdengine.beattable;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;

@TestedFeature("Vote")
@TestedOperation("calculate winners")
@TestedBehaviour("only choices not in ignoredChoices are considered")
@RunWith(MockitoJUnitRunner.Silent.class)
public class BeatTableIgnoreServiceTest {

  @InjectMocks
  private BeatTableIgnoreServiceImpl beatTableIgnoreService;

  private Collection<String> ignoredChoices;
  private BeatTable ignoredBeatTable;

  private BeatTableData beatTableData;

  @Before
  public void setUp() {
    beatTableData = new BeatTableData();

    ignoredChoices = List.of(VoteData.CHOICE1, VoteData.CHOICE2);
    beatTableData.beatTableComplex
        .setElement(VoteData.CHOICE3, VoteData.CHOICE3, new Pair(42, 69));
    ignoredBeatTable =
        beatTableIgnoreService
            .ignoreChoices(beatTableData.beatTableComplex, ignoredChoices);
  }

  @Test
  public void ignore_choices_returns_none_of_the_ignored_choices() {
    assertIntersectionIsEmpty(
        ignoredChoices, ignoredBeatTable.getKeyCollection()
    );
  }

  @Test
  public void ignore_choices_returns_the_not_ignored_choices() {
    assertBeatTableContainsChoice(VoteData.CHOICE3);
  }

  private void assertBeatTableContainsChoice(final String choice) {
    assertTrue(ignoredBeatTable.getKeyCollection().contains(choice));
    assertEquals(
        beatTableData.beatTableComplex.getElement(choice, choice), ignoredBeatTable.getElement(choice, choice)
    );
  }

  @Test
  public void ignore_choices_copies_every_not_ignored_pairs() {
    assertBeatsEqualsInSubset(
        beatTableData.beatTableComplex, ignoredBeatTable,
        Set.of(VoteData.CHOICE3)
    );
  }

  private void assertBeatsEqualsInSubset(
      final BeatTable table1, final BeatTable table2,
      final Set<String> choices
  ) {
    for (final String choice1 : choices)
      for (final String choice2 : choices)
        assertEquals(
            table1.getElement(choice1, choice2), table2.getElement(choice1, choice2)
        );
  }

  private void assertIntersectionIsEmpty(
      final Collection<String> collection1,
      final Collection<String> collection2
  ) {
    assertFalse(
        Set.copyOf(collection1).stream().anyMatch(Set.copyOf(collection2)::contains)
    );
  }
}
