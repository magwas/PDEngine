package org.rulez.demokracia.pdengine.ballot;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.exception.ReportedException;

@TestedFeature("Manage votes")
@TestedOperation("Obtain ballot")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ObtainBallotTest extends ObtainBallotTestBase {

  private static final String ANON_USER = "user";

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @TestedBehaviour("creates a new ballot with an id for the vote")
  @Test
  public void obtain_ballot_returns_the_ballot_string() {
    final String ballot = ballotService.obtainBallot(
        voteData.voteWithOneAssuranceWeHave,
        voteData.voteWithOneAssuranceWeHave.getAdminKey()
    );
    assertTrue(ballot instanceof String);
  }

  @TestedBehaviour("creates a new ballot with an id for the vote")
  @Test
  public void two_ballots_are_different() {
    final String ballot1 = ballotService.obtainBallot(
        voteData.voteWithOneAssuranceWeHave,
        voteData.voteWithOneAssuranceWeHave.getAdminKey()
    );
    final String ballot2 = ballotService.obtainBallot(
        voteData.voteWithOneAssuranceWeHave,
        voteData.voteWithOneAssuranceWeHave.getAdminKey()
    );
    assertNotEquals(ballot1, ballot2);
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void adminKey_is_checked() {

    assertThrows(
        () -> ballotService
            .obtainBallot(
                voteData.voteWithOneAssuranceWeHave,
                invalidAdminKey
            )
    )
        .assertException(ReportedException.class)
        .assertMessageIs("IllegalKey");

  }

  @TestedBehaviour("creates a new ballot with an id for the vote")
  @Test
  public void obtainBallot_stores_the_ballot() {
    final String ballot = ballotService.obtainBallot(
        voteData.voteWithOneAssuranceWeHave,
        voteData.voteWithOneAssuranceWeHave.getAdminKey()
    );
    assertTrue(
        voteData.voteWithOneAssuranceWeHave.getBallots()
            .contains(ballot)
    );
  }

  @TestedBehaviour(
    "the number of ballots obtained with adminKey are recorded for \"admin\""
  )
  @Test
  public void obtainBallot_increases_recordedBallots_when_adminKey_is_admin() {
    final String adminKey =
        voteData.voteWithOneAssuranceWeHave.getAdminKey();
    final int originalObtainedBallots =
        voteData.voteWithOneAssuranceWeHave
            .getRecordedBallotsCount(adminKey);
    ballotService.obtainBallot(
        voteData.voteWithOneAssuranceWeHave, adminKey
    );
    assertEquals(
        originalObtainedBallots + 1,
        voteData.voteWithOneAssuranceWeHave
            .getRecordedBallotsCount("admin").intValue()
    );
  }

  @TestedBehaviour(
    "the number of ballots obtained with anon adminkey are recorded with the proxy id of the user"
  )
  @Test
  public void obtainBallot_increases_recordedBallots_when_adminKey_is_anon() {
    final int originalObtainedBallots =
        voteData.voteWithOneAssuranceWeHave
            .getRecordedBallotsCount(ANON_USER);
    ballotService.obtainBallot(
        voteData.voteWithOneAssuranceWeHave, ANON_USER
    );
    assertEquals(
        originalObtainedBallots + 1,
        voteData.voteWithOneAssuranceWeHave
            .getRecordedBallotsCount(ANON_USER).intValue()
    );
  }
}
