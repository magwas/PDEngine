package org.rulez.demokracia.pdengine.dataobjects;

import java.util.List;

import org.rulez.demokracia.pdengine.choice.Choice;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;
import org.rulez.demokracia.pdengine.vote.Vote;
import org.rulez.demokracia.pdengine.vote.VoteServiceContract;
import org.rulez.demokracia.pdengine.votecast.CastVote;

public class VoteData implements VoteServiceContract {

  final public static String invalidvoteId = "invalidVoteId";

  public final static String CHOICE1 = "choice1";
  public final static String CHOICE2 = "choice2";
  public final static String CHOICE3 = "choice3";
  public final static String CHOICE4 = "choice4";
  public final static String CHOICE5 = "choice5";

  public Vote voteWithOneNeededAssuranceWeHave;
  public VoteAdminInfo adminInfoForVoteWithOneNeededAssuranceWeHave;

  public Vote voteWithTwoNeededAssurancesWeHave;

  public Vote voteWithTwoNeededAssurancesWeDontHaveOne;
  public VoteAdminInfo adminInfoForVoteWeDontHaveOneAssurance;

  public Vote voteWithNoNeededAssurances;
  public Choice choice;
  public final Choice choiceOfAuthenticatedUser;
  public VoteAdminInfo adminInfoWithNoNeededAssurances;
  public VoteAdminInfo adminInfoWithInvalidVoteId;
  public VoteAdminInfo adminInfoWithInvalidAdminKey;
  public VoteAdminInfo adminInfoWithNoNeededAssurancesWithUser;

  public Vote voteWithNeededAssuranceWeDontHave;
  public VoteAdminInfo adminInfoForVoteWeDontHaveAssurance;

  public Vote voteUnmodifiable;
  public VoteAdminInfo adminInfoUnmodifiable;

  public final Vote voteWithCountedAssuranceWeDontHave;
  public final VoteAdminInfo adminInfoForVoteWeDontHaveCountedAssurance;

  public final Vote voteWithTwoCountedAssurancesWeDontHaveOne;
  public final VoteAdminInfo adminInfoForVoteWeDontHaveOneCountedAssurance;

  public final Vote voteWithOneCountedAssuranceWeHave;
  public final VoteAdminInfo adminInfoForVoteWithOneCountedAssuranceWeHave;

  public final CastVoteData castVoteData;

  public final BeatTableData beatTableData;

  public VoteData() {

    castVoteData = new CastVoteData();
    beatTableData = new BeatTableData();
    final List<CastVote> votesCast = castVoteData.castVoteList;

    voteWithTwoNeededAssurancesWeHave =
        createVote(
            List.of(
                ASSURANCE_WE_HAVE,
                ANOTHER_ASSURANCE_WE_HAVE
            ),
            List.of(
                ASSURANCE_WE_HAVE,
                ANOTHER_ASSURANCE_WE_HAVE
            )

        );

    voteWithTwoNeededAssurancesWeDontHaveOne =
        createVote(
            List.of(
                ASSURANCE_WE_HAVE,
                ASSURANCE_WE_DONT_HAVE
            ),
            List.of(
                ASSURANCE_WE_HAVE,
                ASSURANCE_WE_DONT_HAVE
            )
        );
    adminInfoForVoteWeDontHaveOneAssurance = new VoteAdminInfo(
        voteWithTwoNeededAssurancesWeDontHaveOne.getId(),
        voteWithTwoNeededAssurancesWeDontHaveOne.getAdminKey()
    );

    voteWithTwoCountedAssurancesWeDontHaveOne =
        createVote(
            List.of(
                ASSURANCE_WE_HAVE
            ),
            List.of(
                ASSURANCE_WE_HAVE,
                ASSURANCE_WE_DONT_HAVE
            )
        );
    voteWithTwoCountedAssurancesWeDontHaveOne.setVotesCast(votesCast);
    adminInfoForVoteWeDontHaveOneCountedAssurance = new VoteAdminInfo(
        voteWithTwoCountedAssurancesWeDontHaveOne.getId(),
        voteWithTwoCountedAssurancesWeDontHaveOne.getAdminKey()
    );

    voteUnmodifiable =
        createVote(List.of(ASSURANCE_WE_HAVE), List.of(ASSURANCE_WE_HAVE));
    voteUnmodifiable.addBallot("ballot");
    adminInfoUnmodifiable = new VoteAdminInfo(
        voteUnmodifiable.getId(),
        voteUnmodifiable.getAdminKey()
    );

    voteWithNoNeededAssurances =
        createVote(List.of(), List.of(ASSURANCE_WE_HAVE));
    choice = new Choice("choiceName", OTHER_USER);
    choiceOfAuthenticatedUser =
        new Choice("choiceName", AUTHENTICATED_USER_NAME);
    voteWithNoNeededAssurances.addChoice(choice);
    voteWithNoNeededAssurances.addChoice(choiceOfAuthenticatedUser);
    adminInfoWithNoNeededAssurancesWithUser = new VoteAdminInfo(
        voteWithNoNeededAssurances.getId(), USER
    );

    adminInfoWithInvalidVoteId =
        new VoteAdminInfo(
            invalidvoteId, voteWithNoNeededAssurances.getAdminKey()
        );
    adminInfoWithInvalidAdminKey =
        new VoteAdminInfo(voteWithNoNeededAssurances.getId(), BAD_ADMINKEY);

    adminInfoWithNoNeededAssurances = new VoteAdminInfo(
        voteWithNoNeededAssurances.getId(),
        voteWithNoNeededAssurances.getAdminKey()
    );

    voteWithNeededAssuranceWeDontHave =
        createVote(List.of(ASSURANCE_WE_DONT_HAVE), List.of(ASSURANCE_WE_DONT_HAVE));
    adminInfoForVoteWeDontHaveAssurance = new VoteAdminInfo(
        voteWithNeededAssuranceWeDontHave.getId(),
        voteWithNeededAssuranceWeDontHave.getAdminKey()
    );

    voteWithCountedAssuranceWeDontHave =
        createVote(List.of(ASSURANCE_WE_HAVE), List.of(ASSURANCE_WE_DONT_HAVE));
    adminInfoForVoteWeDontHaveCountedAssurance = new VoteAdminInfo(
        voteWithCountedAssuranceWeDontHave.getId(),
        voteWithCountedAssuranceWeDontHave.getAdminKey()
    );

    voteWithOneNeededAssuranceWeHave =
        createVote(List.of(ASSURANCE_WE_HAVE), List.of(ASSURANCE_WE_HAVE));
    adminInfoForVoteWithOneNeededAssuranceWeHave = new VoteAdminInfo(
        voteWithOneNeededAssuranceWeHave.getId(),
        voteWithOneNeededAssuranceWeHave.getAdminKey()
    );

    voteWithOneCountedAssuranceWeHave =
        createVote(List.of(ASSURANCE_WE_HAVE, ASSURANCE_WE_DONT_HAVE), List.of(ASSURANCE_WE_HAVE));
    final Choice choice2 = new Choice("choiceName", OTHER_USER);
    voteWithOneCountedAssuranceWeHave.addChoice(choice2);
    adminInfoForVoteWithOneCountedAssuranceWeHave = new VoteAdminInfo(
        voteWithOneCountedAssuranceWeHave.getId(),
        voteWithOneCountedAssuranceWeHave.getAdminKey()
    );

  }

  private Vote createVote(
      final List<String> neededAssurances, final List<String> countedAssurances
  ) {
    return new Vote(
        "HolVoteHolNemVote", neededAssurances, countedAssurances, false, 1
    );
  }

}
