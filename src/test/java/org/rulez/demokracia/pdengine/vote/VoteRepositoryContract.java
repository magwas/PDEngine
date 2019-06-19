package org.rulez.demokracia.pdengine.vote;

import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.rulez.demokracia.pdengine.dataobjects.VoteData;

public interface VoteRepositoryContract {

  default void
      contract(final VoteRepository voteRepository, final VoteData voteData) {
    doReturn(Optional.empty())
        .when(voteRepository)
        .findById(VoteData.invalidvoteId);
    doReturn(Optional.of(voteData.voteWithOneNeededAssuranceWeHave))
        .when(voteRepository)
        .findById(voteData.voteWithOneNeededAssuranceWeHave.getId());
    doReturn(Optional.of(voteData.voteWithNoNeededAssurances))
        .when(voteRepository)
        .findById(voteData.voteWithNoNeededAssurances.getId());
    doReturn(Optional.of(voteData.voteUnmodifiable))
        .when(voteRepository)
        .findById(voteData.voteUnmodifiable.getId());
  }

}
