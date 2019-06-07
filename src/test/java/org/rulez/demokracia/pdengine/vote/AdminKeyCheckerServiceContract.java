package org.rulez.demokracia.pdengine.vote;

import static org.mockito.Mockito.*;

import org.rulez.demokracia.pdengine.authentication.AuthServiceContract;
import org.rulez.demokracia.pdengine.exception.ReportedException;

public interface AdminKeyCheckerServiceContract extends AuthServiceContract {

  String invalidAdminKey = "illegalAdminKey";

  default void contract(
      final AdminKeyCheckerService adminKeyCheckerService,
      final VoteData voteData
  ) {

    doThrow(new ReportedException("IllegalKey")).when(adminKeyCheckerService)
        .checkAdminKey(
            voteData.voteWithOneAssuranceWeHave,
            invalidAdminKey
        );

    doNothing().when(adminKeyCheckerService)
        .checkAdminKey(
            voteData.voteWithOneAssuranceWeHave,
            AUTHENTICATED_USER_NAME
        );

  }

}
