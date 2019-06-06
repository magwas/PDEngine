package org.rulez.demokracia.pdengine.vote;

import org.rulez.demokracia.pdengine.exception.ReportedException;
import org.springframework.stereotype.Service;

@Service
public class AdminKeyCheckerServiceImpl implements AdminKeyCheckerService {

  @Override
  public void checkAdminKey(final Vote vote, final String providedAdminKey) {
    if (
      !(vote.getAdminKey().equals(providedAdminKey) ||
          "user".equals(providedAdminKey))
    )
      throw new ReportedException("Illegal adminKey", providedAdminKey);
  }

}
