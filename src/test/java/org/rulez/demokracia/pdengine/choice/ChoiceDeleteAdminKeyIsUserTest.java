package org.rulez.demokracia.pdengine.choice;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.exception.ReportedException;

@TestedFeature("Manage votes")
@TestedOperation("delete choice")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ChoiceDeleteAdminKeyIsUserTest extends ChoiceTestBase {

  private static final String IF_THE_VOTE_HAS_BALLOTS_ISSUED =
      "if the vote has ballots issued, the choice cannot be deleted";
  private static final String IF_USER_IS_USED_AS_ADMIN_KEY =
      "if \"user\" is used as adminKey, then the user must be the one who added the choice and canAddIn be true";
  private static final String CHOICE1 = "choice1";

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @TestedBehaviour(IF_USER_IS_USED_AS_ADMIN_KEY)
  @Test
  public void if_canAddin_is_false_then_other_users_cannot_delete_choices() {
    final Choice choiceToDelete = createChoice(AUTHENTICATED_USER_NAME, false);
    assertThrows(
        () -> choiceService.deleteChoice(
            voteData.voteAdminInfoWithUser,
            choiceToDelete.getId()
        )
    ).assertMessageIs("The adminKey is \"user\" but canAddin is false.");
  }

  @TestedBehaviour(IF_USER_IS_USED_AS_ADMIN_KEY)
  @Test
  public void
      if_adminKey_is_user_and_the_user_is_not_the_one_who_added_the_choice_then_the_choice_cannot_be_deleted() {
    final Choice choiceToDelete = createChoice("other user", true);
    assertThrows(
        () -> choiceService
            .deleteChoice(voteData.voteAdminInfoWithUser, choiceToDelete.getId())
    ).assertMessageIs(
        "The adminKey is \"user\" but the user is not same with that user who added the choice."
    );
  }

  @TestedBehaviour(IF_USER_IS_USED_AS_ADMIN_KEY)
  @Test
  public void
      if_adminKey_is_user_and_canAddin_is_true_then_the_user_who_added_the_choice_is_able_to_delete_it() {
    final Choice choiceToDelete = createChoice(AUTHENTICATED_USER_NAME, true);
    when(authService.getAuthenticatedUserName())
        .thenReturn(AUTHENTICATED_USER_NAME);

    final VoteAdminInfo voteAdminInfo =
        new VoteAdminInfo(
            voteData.voteWithNoAssurances.getId(), AUTHENTICATED_USER_NAME
        );
    when(voteService.getModifiableVote(voteAdminInfo))
        .thenReturn(voteData.voteWithNoAssurances);
    choiceService.deleteChoice(voteAdminInfo, choiceToDelete.getId());

    assertThrows(
        () -> choiceService.getChoice(
            voteData.voteWithNoAssurances.getId(), choiceToDelete.getId()
        )
    )
        .assertMessageIs("Illegal choiceId");
  }

  @TestedBehaviour(IF_USER_IS_USED_AS_ADMIN_KEY)
  @Test
  public void deleteChoice_saves_vote_if_the_choice_is_deleted() {
    final Choice choiceToDelete = createChoice(AUTHENTICATED_USER_NAME, true);
    when(authService.getAuthenticatedUserName())
        .thenReturn(AUTHENTICATED_USER_NAME);
    choiceService.deleteChoice(
        voteData.voteAdminInfo, choiceToDelete.getId()
    );
    verify(voteService).saveVote(voteData.voteWithNoAssurances);
  }

  @TestedBehaviour(IF_THE_VOTE_HAS_BALLOTS_ISSUED)
  @Test
  public void
      if_the_vote_is_not_modifiable_then_the_choice_cannot_be_deleted() {
    final Choice choiceToDelete = createChoice(AUTHENTICATED_USER_NAME, true);
    doThrow(
        new ReportedException(
            "nonmodifiable"
        )
    ).when(voteService)
        .getModifiableVote(voteData.voteAdminInfo);

    assertThrows(
        () -> choiceService
            .deleteChoice(voteData.voteAdminInfo, choiceToDelete.getId())
    ).assertMessageIs("nonmodifiable");
  }

  private Choice
      createChoice(final String userName, final boolean isAddinable) {
    voteData.voteWithNoAssurances.getParameters().setAddinable(isAddinable);
    final Choice choiceToDelete = new Choice(CHOICE1, userName);
    voteData.voteWithNoAssurances.addChoice(choiceToDelete);
    return choiceToDelete;
  }
}
