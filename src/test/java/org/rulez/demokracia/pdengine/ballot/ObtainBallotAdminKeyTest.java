package org.rulez.demokracia.pdengine.ballot;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;

@TestedFeature("Manage votes")
@TestedOperation("Obtain ballot")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ObtainBallotAdminKeyTest extends ObtainBallotTestBase {

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @TestedBehaviour(
    "if adminKey is anon, the user should have all the neededAssurances"
  )
  @Test
  public void
      if_the_user_does_not_have_all_the_needed_assurances_then_she_cannot_vote() {

    assertThrows(
        () -> ballotService
            .obtainBallot(
                voteData.voteWithAssuranceWeDontHave,
                AUTHENTICATED_USER_NAME
            )
    )
        .assertMessageIs("The user does not have all of the needed assurances.");
  }

  @TestedBehaviour(
    "if adminKey is anon, the user should have all the neededAssurances"
  )
  @Test
  public void
      if_the_user_does_have_all_the_assurances_then_a_ballot_is_served() {
    final String ballot =
        ballotService
            .obtainBallot(
                voteData.voteWithTwoAssurancesWeHave,
                AUTHENTICATED_USER_NAME
            );
    assertTrue(ballot instanceof String);
  }

  @TestedBehaviour(
    "if adminKey is anon, the user should have all the neededAssurances"
  )
  @Test
  public void if_neededAssurances_is_empty_then_a_ballot_is_served_to_anyone() {
    final String ballot =
        ballotService.obtainBallot(
            voteData.voteWithNoAssurances,
            AUTHENTICATED_USER_NAME
        );
    assertTrue(ballot instanceof String);
  }

  @TestedBehaviour("if adminkey is anon, only one ballot can be issued")
  @Test
  public void
      even_if_the_user_does_have_all_the_assurances_he_cannot_issue_more_than_one_ballot() {

    ballotService
        .obtainBallot(
            voteData.voteWithOneAssuranceWeHave,
            AUTHENTICATED_USER_NAME
        );
    assertThrows(
        () -> ballotService
            .obtainBallot(
                voteData.voteWithOneAssuranceWeHave,
                AUTHENTICATED_USER_NAME
            )
    )
        .assertMessageIs("This user already have a ballot.");
  }

  @TestedBehaviour("Admin can obtain more ballots")
  @Test
  public void admin_can_obtain_more_ballots() {

    final String ballotAdmin1 =
        ballotService.obtainBallot(
            voteData.voteWithOneAssuranceWeHave,
            voteData.voteWithOneAssuranceWeHave.getAdminKey()
        );
    final String ballotAdmin2 =
        ballotService.obtainBallot(
            voteData.voteWithOneAssuranceWeHave,
            voteData.voteWithOneAssuranceWeHave.getAdminKey()
        );
    assertNotEquals(ballotAdmin1, ballotAdmin2);
  }

  @TestedBehaviour(
    "if the adminKey is anon and the user is not logged in then no ballots are issued"
  )
  @Test
  public void not_logged_in_user_cannot_issue_any_ballot() {
    context.username = null;

    assertThrows(
        () -> ballotService
            .obtainBallot(
                voteData.voteWithOneAssuranceWeHave,
                AUTHENTICATED_USER_NAME
            )
    )
        .assertMessageIs(
            "Simple user is not authenticated, cannot issue any ballot."
        );
  }
}
