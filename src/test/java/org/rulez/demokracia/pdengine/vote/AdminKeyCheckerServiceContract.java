package org.rulez.demokracia.pdengine.vote;

import static org.mockito.Mockito.*;

import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.exception.ReportedException;

public interface AdminKeyCheckerServiceContract extends VoteServiceContract {

  String ILLEGAL_KEY = "IllegalKey";

  default void contract(
      final AdminKeyCheckerService adminKeyCheckerService,
      final VoteData voteData
  ) {

    doThrow(new ReportedException(ILLEGAL_KEY)).when(adminKeyCheckerService)
        .checkAdminKey(
            voteData.voteWithNoNeededAssurances,
            BAD_ADMINKEY
        );

    doNothing().when(adminKeyCheckerService)
        .checkAdminKey(
            voteData.voteWithOneNeededAssuranceWeHave,
            AUTHENTICATED_USER_NAME
        );

  }

}
