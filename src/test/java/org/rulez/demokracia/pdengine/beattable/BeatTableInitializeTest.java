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
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;

@TestedFeature("Schulze method")
@TestedOperation("compute initial beat matrix")
@TestedBehaviour("the beat matrix contains the beats")
@RunWith(MockitoJUnitRunner.Silent.class)
public class BeatTableInitializeTest extends ThrowableTester {

  @InjectMocks
  private BeatTableServiceImpl beatTableService;

  private VoteData voteData;
  private BeatTable beatTable;

  @Before
  public void setUp() {
    voteData = new VoteData();

    beatTable = beatTableService
        .initializeBeatTable(voteData.castVoteData.castVoteList);
  }

  @Test
  public void initialize_throws_exception_when_param_is_not_defined() {
    assertThrows(() -> beatTableService.initializeBeatTable(null))
        .assertMessageIs("Invalid castVotes");
  }

  @Test
  public void
      initialize_does_not_modify_the_values_when_there_is_no_vote() {
    assertEquals(
        new Pair(0, 0), beatTable.getElement(VoteData.CHOICE4, VoteData.CHOICE5)
    );
  }

  @Test
  public void
      initialize_sets_the_right_value_FORWARD() {
    assertEquals(
        voteData.beatTableData.beatTableComplex
            .getElement(VoteData.CHOICE2, VoteData.CHOICE3),
        beatTable.getElement(VoteData.CHOICE2, VoteData.CHOICE3)
    );
  }

  @Test
  public void
      initialize_sets_the_right_value_BACKWARD() {
    assertEquals(
        voteData.beatTableData.beatTableComplex
            .getElement(VoteData.CHOICE3, VoteData.CHOICE2),
        beatTable.getElement(VoteData.CHOICE3, VoteData.CHOICE2)
    );
  }

  @Test
  public void initialize_initializes_correctly() {
    final BeatTable beatTable =
        beatTableService.initializeBeatTable(voteData.castVoteData.castVoteList);
    assertEquals(
        voteData.beatTableData.beatTableComplex,
        beatTable
    );
  }
}
