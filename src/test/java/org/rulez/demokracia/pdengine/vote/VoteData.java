package org.rulez.demokracia.pdengine.vote;

import java.util.ArrayList;
import java.util.List;

import org.rulez.demokracia.pdengine.choice.Choice;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;

public class VoteData implements VoteServiceContract {

  public Vote voteWithOneAssuranceWeHave;
  public Vote voteWithTwoAssurancesWeHave;
  public Vote voteWithNoAssurances;
  public Vote voteWithAssuranceWeDontHave;

  public VoteAdminInfo voteAdminInfo;
  public VoteAdminInfo voteAdminInfoWithUser;
  public VoteAdminInfo adminInfoWithInvalidVoteId;

  public Choice choice;

  public VoteData() {

    voteWithOneAssuranceWeHave =
        createVote(List.of(ASSURANCE_WE_HAVE));
    voteWithTwoAssurancesWeHave =
        createVote(
            List.of(
                ASSURANCE_WE_HAVE,
                ANOTHER_ASSURANCE_WE_HAVE
            )
        );
    choice = new Choice("choiceName", OTHER_USER);

    voteWithNoAssurances = createVote(List.of());
    voteWithNoAssurances.addChoice(choice);

    voteWithAssuranceWeDontHave =
        createVote(List.of(ASSURANCE_WE_DONT_HAVE));
    adminInfoWithInvalidVoteId =
        new VoteAdminInfo(
            invalidvoteId, voteWithNoAssurances.getAdminKey()
        );
    voteAdminInfo = new VoteAdminInfo(
        voteWithNoAssurances.getId(),
        voteWithNoAssurances.getAdminKey()
    );
    voteAdminInfoWithUser = new VoteAdminInfo(
        voteWithNoAssurances.getId(), AUTHENTICATED_USER_NAME
    );
  }

  Vote createVote(final List<String> neededAssurances) {
    return new Vote(
        "HolVoteHolNemVote", neededAssurances, new ArrayList<>(), false, 1
    );
  }

}
