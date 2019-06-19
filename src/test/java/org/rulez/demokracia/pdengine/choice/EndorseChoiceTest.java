package org.rulez.demokracia.pdengine.choice;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.exception.ReportedException;

@TestedFeature("Vote")
@TestedOperation("Endorse option")
@RunWith(MockitoJUnitRunner.Silent.class)
public class EndorseChoiceTest extends ChoiceTestBase {

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @TestedBehaviour(
    "if adminKey is not user, the userName is registered " +
        "as endorserName for the choice"
  )
  @Test
  public void endorsement_is_registered() {
    choiceService.endorseChoice(
        voteData.adminInfoWithNoNeededAssurances, voteData.choice.getId(),
        "testuser"
    );
    assertTrue(voteData.choice.getEndorsers().contains("testuser"));
  }

  @TestedBehaviour(
    "if adminKey is 'user', then canEndorse must be true," +
        " and the proxy id of the user will be registered as endorserName for the choice"
  )
  @Test
  public void
      if_adminKey_is_user_and_canEndorse_is_false_then_an_exception_is_thrown() {
    assertThrows(
        () -> choiceService.endorseChoice(
            new VoteAdminInfo(
                voteData.voteWithNoNeededAssurances.getId(), "user"
            ),
            voteData.choice.getId(), AUTHENTICATED_USER_NAME
        )
    ).assertException(ReportedException.class);
  }

  @TestedBehaviour(
    "if adminKey is 'user', then canEndorse must be true," +
        " and the proxy id of the user will be registered as endorserName for the choice"
  )
  @Test
  public void
      if_adminKey_is_user_then_the_proxy_id_is_registered_for_the_vote() {
    voteData.voteWithNoNeededAssurances.getParameters().setEndorsable(true);
    choiceService
        .endorseChoice(
            new VoteAdminInfo(
                voteData.voteWithNoNeededAssurances.getId(), "user"
            ), voteData.choice.getId(),
            AUTHENTICATED_USER_NAME
        );
    assertTrue(
        voteData.choice.getEndorsers().contains(AUTHENTICATED_USER_NAME)
    );
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void no_way_to_endorse_choice_with_invalid_vote_id() {
    assertThrows(
        () -> choiceService
            .endorseChoice(
                voteData.adminInfoWithInvalidVoteId,
                voteData.choice.getId(), AUTHENTICATED_USER_NAME
            )
    ).assertMessageIs(ILLEGAL_VOTE_ID);
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void invalid_adminKey_is_rejected() {

    assertThrows(
        () -> choiceService
            .endorseChoice(
                voteData.adminInfoWithInvalidAdminKey,
                voteData.choice.getId(), AUTHENTICATED_USER_NAME
            )
    ).assertMessageIs(ILLEGAL_KEY);
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void invalid_choiceId_is_rejected() {
    assertThrows(
        () -> choiceService
            .endorseChoice(
                voteData.adminInfoWithNoNeededAssurances,
                "illegalChoice", AUTHENTICATED_USER_NAME
            )
    ).assertMessageIs("Illegal choiceId");
  }
}
