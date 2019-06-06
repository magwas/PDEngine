package org.rulez.demokracia.pdengine.choice;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.exception.ReportedException;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;
import org.rulez.demokracia.pdengine.testhelpers.VariantVote;
import org.rulez.demokracia.pdengine.vote.AdminKeyCheckerService;
import org.rulez.demokracia.pdengine.vote.Vote;
import org.rulez.demokracia.pdengine.vote.VoteService;

@TestedFeature("Manage votes")
@TestedOperation("Add choice")
@RunWith(MockitoJUnitRunner.class)
public class ChoiceAddValidationTest extends ThrowableTester {

  @InjectMocks
  private ChoiceServiceImpl choiceService;

  @Mock
  private VoteService voteService;

  @Mock
  private AdminKeyCheckerService adminKeyCheckerService;

  private final Vote vote = new VariantVote();

  @Before
  public void setUp() {
  }

  @TestedBehaviour("validates inputs")
  @Test
  public void invalid_vote_admin_info_is_rejected() {
    final VoteAdminInfo voteAdminInfo = mock(VoteAdminInfo.class);
    doThrow(new ReportedException("IllegalKey")).when(voteService)
        .getModifiableVote(voteAdminInfo);

    assertaddChoiceThrowsUp(voteAdminInfo)
        .assertMessageIs("IllegalKey");
  }

  @TestedBehaviour(
    "No choice can be added if the vote is not modifiable."
  )
  @Test
  public void no_choice_can_be_added_if_the_vote_is_not_modifiable() {
    final VoteAdminInfo voteAdminInfo =
        new VoteAdminInfo(vote.getId(), vote.getAdminKey());
    doThrow(new ReportedException("unmodifiable"))
        .when(voteService)
        .getModifiableVote(voteAdminInfo);
    assertaddChoiceThrowsUp(voteAdminInfo)
        .assertMessageIs("unmodifiable");
  }

  private ThrowableTester
      assertaddChoiceThrowsUp(final VoteAdminInfo voteAdminInfo) {
    return assertThrows(() -> {
      choiceService
          .addChoice(voteAdminInfo, "choice1", "user");
    });
  }
}
