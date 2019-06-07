package org.rulez.demokracia.pdengine.vote;

import static org.mockito.Mockito.*;

import org.rulez.demokracia.pdengine.authentication.AuthServiceContract;
import org.rulez.demokracia.pdengine.exception.ReportedException;

public interface VoteServiceContract extends AuthServiceContract {

  String invalidvoteId = "invalidVoteId";

  default void
      contract(final VoteService voteService, final VoteData voteData) {
    when(voteService.getVote(voteData.voteWithNoAssurances.getId()))
        .thenReturn(voteData.voteWithNoAssurances);
    doReturn(voteData.voteWithNoAssurances).when(voteService)
        .getModifiableVote(voteData.voteAdminInfo);
    when(voteService.getModifiableVote(voteData.voteAdminInfoWithUser))
        .thenReturn(voteData.voteWithNoAssurances);

    doThrow(new ReportedException("illegal voteId", invalidvoteId))
        .when(voteService)
        .getModifiableVote(voteData.adminInfoWithInvalidVoteId);

  }

}
