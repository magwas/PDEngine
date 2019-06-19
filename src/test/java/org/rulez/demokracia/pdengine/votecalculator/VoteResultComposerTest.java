package org.rulez.demokracia.pdengine.votecalculator;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;

@TestedFeature("Vote")
@TestedOperation("Compute vote results")
@TestedBehaviour("the winners list contains the looses to the first one")
@RunWith(MockitoJUnitRunner.Silent.class)
public class VoteResultComposerTest extends VoteResultTestBase {

  @Before
  @Override
  public void setUp() {
    super.setUp();
    choicesReturned = convertResultToChoiceSet(result);

  }

  @Test
  public void compute_vote_results_returns_every_choice() {
    assertEquals(
        new HashSet<>(BeatTableData.allChoices), new HashSet<>(choicesReturned)
    );
  }

  @Test
  public void compute_vote_results_returns_each_choices_once() {
    final List<String> keyList = result.stream().map(VoteResult::getWinners)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    System.out.println(keyList);
    assertEquals(keyList.size(), choicesReturned.size());
  }

  @Test
  public void compute_vote_results_assigns_no_beat_to_winners() {
    final int winnersLoses =
        result.get(0).getBeats().values().stream().map(m -> m.getBeats().size())
            .reduce((a, b) -> a + b).get();
    assertEquals(0, winnersLoses);
  }

  @Test
  public void compute_vote_results_return_nonzero_loses_for_nonwinners() {
    for (final VoteResult choiceMap : result.subList(1, result.size()))
      assertEachChoiceHaveBeaten(choiceMap);
  }

  private void assertEachChoiceHaveBeaten(final VoteResult voteResult) {
    assertTrue(
        voteResult.getBeats().values().stream()
            .allMatch(m -> !m.getBeats().isEmpty())
    );
  }

  private Set<String> convertResultToChoiceSet(final List<VoteResult> result) {
    return result.stream().map(voteResult -> voteResult.getWinners())
        .flatMap(List::stream)
        .collect(Collectors.toSet());
  }
}
