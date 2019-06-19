package org.rulez.demokracia.pdengine.vote;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.rulez.demokracia.pdengine.authentication.AuthServiceContract;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.exception.ReportedException;

public interface VoteServiceContract extends AuthServiceContract {

  String BAD_ADMINKEY = "bad adminkey (mock)";
  String ILLEGAL_VOTE_ID = "iillegal voteId (mock)";
  String UNMODIFIABLE = "unmodifiable (mock)";
  String MISSING_ASSURANCES =
      "missing assurances (mock)";
  String invalidvoteId = VoteData.invalidvoteId;

  default void
      contract(final VoteService voteService, final VoteData voteData) {
    doReturn(voteData.voteWithNoNeededAssurances)
        .when(voteService)
        .getVote(voteData.voteWithNoNeededAssurances.getId());
    doThrow(new RuntimeException(ILLEGAL_VOTE_ID))
        .when(voteService)
        .getVote(invalidvoteId);

    doReturn(voteData.voteWithNoNeededAssurances)
        .when(voteService)
        .getModifiableVote(voteData.adminInfoWithNoNeededAssurances);
    doReturn(voteData.voteWithNoNeededAssurances)
        .when(voteService)
        .getModifiableVote(voteData.adminInfoWithNoNeededAssurancesWithUser);
    doReturn(voteData.voteUnmodifiable)
        .when(voteService)
        .getModifiableVote(voteData.adminInfoUnmodifiable);
    doThrow(new ReportedException(ILLEGAL_VOTE_ID))
        .when(voteService)
        .getModifiableVote(voteData.adminInfoWithInvalidVoteId);
    doThrow(new ReportedException(UNMODIFIABLE))
        .when(voteService)
        .getModifiableVote(voteData.adminInfoUnmodifiable);
    doThrow(new ReportedException(BAD_ADMINKEY))
        .when(voteService)
        .getModifiableVote(voteData.adminInfoWithInvalidAdminKey);

    doThrow(new ReportedException(MISSING_ASSURANCES))
        .when(voteService)
        .getVoteWithValidatedAdminKey(
            voteData.adminInfoForVoteWeDontHaveCountedAssurance
        );
    doThrow(new ReportedException(MISSING_ASSURANCES))
        .when(voteService)
        .getVoteWithValidatedAdminKey(
            voteData.adminInfoForVoteWeDontHaveOneCountedAssurance
        );
    doReturn(voteData.voteWithOneCountedAssuranceWeHave)
        .when(voteService)
        .getVoteWithValidatedAdminKey(
            voteData.adminInfoForVoteWithOneCountedAssuranceWeHave
        );
    doThrow(new ReportedException(ILLEGAL_VOTE_ID))
        .when(voteService)
        .getVoteWithValidatedAdminKey(
            voteData.adminInfoWithInvalidVoteId
        );
    doThrow(new ReportedException(BAD_ADMINKEY))
        .when(voteService)
        .getVoteWithValidatedAdminKey(
            voteData.adminInfoWithInvalidAdminKey
        );

    doReturn(voteData.adminInfoWithNoNeededAssurances)
        .when(voteService)
        .createVote(any());

  }

}
