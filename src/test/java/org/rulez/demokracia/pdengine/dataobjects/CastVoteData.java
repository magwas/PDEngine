package org.rulez.demokracia.pdengine.dataobjects;

import java.util.ArrayList;
import java.util.List;

import org.rulez.demokracia.pdengine.authentication.AuthServiceContract;
import org.rulez.demokracia.pdengine.choice.RankedChoice;
import org.rulez.demokracia.pdengine.user.User;
import org.rulez.demokracia.pdengine.votecast.CastVote;

public class CastVoteData {

  public final List<CastVote> castVoteList;
  public final List<CastVote> castVoteListOneAssurance;
  public final List<CastVote> castVoteListTwoAssurances;

  public CastVoteData() {
    final String proxyId = "user1";
    final User user = new User(proxyId);
    final List<String> bothAssurances = List.of(
        AuthServiceContract.ASSURANCE_WE_HAVE,
        AuthServiceContract.ANOTHER_ASSURANCE_WE_HAVE
    );
    final List<String> oneAssurance = List.of(
        AuthServiceContract.ASSURANCE_WE_HAVE,
        AuthServiceContract.ANOTHER_ASSURANCE_WE_HAVE
    );
    user.setAssurances(
        bothAssurances
    );
    castVoteListOneAssurance = List.of(
        createCastVote(
            "user2", oneAssurance,
            List.of(
                VoteData.CHOICE1, VoteData.CHOICE4, VoteData.CHOICE2,
                VoteData.CHOICE3
            )
        ),
        createCastVote(
            "user4", oneAssurance,
            List.of(
                VoteData.CHOICE1, VoteData.CHOICE2, VoteData.CHOICE3,
                VoteData.CHOICE4
            )
        ),
        createCastVote(
            "user6", oneAssurance,
            List.of(
                VoteData.CHOICE3, VoteData.CHOICE1, VoteData.CHOICE4,
                VoteData.CHOICE2
            )
        ),
        createCastVote(
            "user7", bothAssurances,
            List.of(
                VoteData.CHOICE3, VoteData.CHOICE2, VoteData.CHOICE1,
                VoteData.CHOICE4
            )
        ),
        createCastVote(
            "user8", oneAssurance,
            List.of(
                VoteData.CHOICE3, VoteData.CHOICE1, VoteData.CHOICE2,
                VoteData.CHOICE5
            )
        ),
        createCastVote(
            "user1", bothAssurances,
            List.of(
                VoteData.CHOICE1, VoteData.CHOICE4, VoteData.CHOICE2,
                VoteData.CHOICE3

            )
        ),
        createCastVote(
            "user10", oneAssurance,
            List.of(
                VoteData.CHOICE5, VoteData.CHOICE1
            )
        ),
        createCastVote(
            "user12", oneAssurance,
            List.of(
                VoteData.CHOICE4, VoteData.CHOICE3
            )
        )
    );
    castVoteListTwoAssurances = List.of(
        createCastVote(
            "user3", bothAssurances,
            List.of(
                VoteData.CHOICE1, VoteData.CHOICE4, VoteData.CHOICE2,
                VoteData.CHOICE3
            )
        ),
        createCastVote(
            "user5", bothAssurances,
            List.of(
                VoteData.CHOICE4, VoteData.CHOICE2, VoteData.CHOICE3,
                VoteData.CHOICE1
            )
        ),
        createCastVote(
            "user9", bothAssurances,
            List.of(
                VoteData.CHOICE1, VoteData.CHOICE4
            )
        ),
        createCastVote(
            "user11", bothAssurances,
            List.of(
                VoteData.CHOICE4, VoteData.CHOICE2
            )
        )
    );
    castVoteList = new ArrayList<>(castVoteListOneAssurance);
    castVoteList.addAll(castVoteListTwoAssurances);
  }

  private CastVote createCastVote(
      final String proxyId, final List<String> assurances,
      final List<String> aVote
  ) {
    final List<RankedChoice> preferences = new ArrayList<>();
    for (int i = 0; i < aVote.size(); i++)
      preferences.add(new RankedChoice(aVote.get(i), i));
    return new CastVote(proxyId, preferences, assurances);
  }

}
