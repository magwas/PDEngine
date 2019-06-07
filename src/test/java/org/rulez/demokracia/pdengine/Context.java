package org.rulez.demokracia.pdengine;

import org.rulez.demokracia.pdengine.authentication.AuthServiceContract;

public class Context implements AuthServiceContract {

  public String username = AUTHENTICATED_USER_NAME;
}
