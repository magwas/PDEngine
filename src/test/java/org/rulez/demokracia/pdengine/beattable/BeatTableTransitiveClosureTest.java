package org.rulez.demokracia.pdengine.beattable;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;

@TestedFeature("Schulze method")
@TestedOperation("compare beats")
@TestedBehaviour("implements the Floyd-Warshall algorithm")
@RunWith(MockitoJUnitRunner.Silent.class)
public class BeatTableTransitiveClosureTest {

  @InjectMocks
  private BeatTableTransitiveClosureServiceImpl beatTableTransitiveClosureService;

  @Before
  public void setUp() {
  }

  @Test
  public void transitive_closure_on_empty_beat_table_results_empty_result() {
    BeatTable actual = new BeatTable();
    actual = beatTableTransitiveClosureService.computeTransitiveClosure(actual);
    assertTrue(actual.getKeyCollection().isEmpty());
  }

}
