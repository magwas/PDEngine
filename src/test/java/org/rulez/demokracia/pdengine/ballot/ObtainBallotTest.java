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

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @TestedBehaviour("creates a new ballot with an id for the vote")
  @Test
  public void obtain_ballot_returns_the_ballot_string() {
    final String ballot = ballotService.obtainBallot(
        voteData.voteWithOneNeededAssuranceWeHave,
        voteData.voteWithOneNeededAssuranceWeHave.getAdminKey()
    );
    assertTrue(ballot instanceof String);
  }

  @TestedBehaviour("creates a new ballot with an id for the vote")
  @Test
  public void two_ballots_are_different() {
    final String ballot1 = ballotService.obtainBallot(
        voteData.voteWithOneNeededAssuranceWeHave,
        voteData.voteWithOneNeededAssuranceWeHave.getAdminKey()
    );
    final String ballot2 = ballotService.obtainBallot(
        voteData.voteWithOneNeededAssuranceWeHave,
        voteData.voteWithOneNeededAssuranceWeHave.getAdminKey()
    );
    assertNotEquals(ballot1, ballot2);
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void adminKey_is_checked() {

    assertThrows(
        () -> ballotService
            .obtainBallot(
                voteData.voteWithNoNeededAssurances,
                BAD_ADMINKEY
            )
    )
        .assertException(ReportedException.class)
        .assertMessageIs("IllegalKey");

  }

  @TestedBehaviour("creates a new ballot with an id for the vote")
  @Test
  public void obtainBallot_stores_the_ballot() {
    final String ballot = ballotService.obtainBallot(
        voteData.voteWithOneNeededAssuranceWeHave,
        voteData.voteWithOneNeededAssuranceWeHave.getAdminKey()
    );
    assertTrue(
        voteData.voteWithOneNeededAssuranceWeHave.getBallots()
            .contains(ballot)
    );
  }

  @TestedBehaviour(
    "the number of ballots obtained with adminKey are recorded for \"admin\""
  )
  @Test
  public void obtainBallot_increases_recordedBallots_when_adminKey_is_admin() {
    final String adminKey =
        voteData.voteWithOneNeededAssuranceWeHave.getAdminKey();
    final int originalObtainedBallots =
        voteData.voteWithOneNeededAssuranceWeHave
            .getRecordedBallotsCount(adminKey);
    ballotService.obtainBallot(
        voteData.voteWithOneNeededAssuranceWeHave, adminKey
    );
    assertEquals(
        originalObtainedBallots + 1,
        voteData.voteWithOneNeededAssuranceWeHave
            .getRecordedBallotsCount("admin").intValue()
    );
  }

  @TestedBehaviour(
    "the number of ballots obtained with anon adminkey are recorded with the proxy id of the user"
  )
  @Test
  public void obtainBallot_increases_recordedBallots_when_adminKey_is_anon() {
    final int originalObtainedBallots =
        voteData.voteWithOneNeededAssuranceWeHave
            .getRecordedBallotsCount(AUTHENTICATED_USER_NAME);
    ballotService.obtainBallot(
        voteData.voteWithOneNeededAssuranceWeHave, USER
    );
    assertEquals(
        originalObtainedBallots + 1,
        voteData.voteWithOneNeededAssuranceWeHave
            .getRecordedBallotsCount(AUTHENTICATED_USER_NAME).intValue()
    );
  }
}
