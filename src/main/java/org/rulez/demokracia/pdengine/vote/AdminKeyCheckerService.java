package org.rulez.demokracia.pdengine.vote;

public interface AdminKeyCheckerService {

  void checkAdminKey(final Vote vote, final String providedAdminKey);

}
