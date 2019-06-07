package org.rulez.demokracia.pdengine.authentication;

import static org.mockito.Mockito.when;

import org.apache.catalina.connector.CoyotePrincipal;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rulez.demokracia.pdengine.Context;

public interface AuthServiceContract {

  String AUTHENTICATED_USER_NAME = "user";
  String OTHER_USER = "other user";

  String ASSURANCE_WE_DONT_HAVE = "dontHave";
  String ASSURANCE_WE_HAVE = "have";
  String ANOTHER_ASSURANCE_WE_HAVE = "haveToo";

  default void contract(
      final AuthenticatedUserService authService,
      final Context context
  ) {
    when(authService.getAuthenticatedUserName())
        .thenReturn(context.username);
    when(authService.getUserPrincipal())
        .thenAnswer(
            new Answer<CoyotePrincipal>() {

              @Override
              public CoyotePrincipal answer(final InvocationOnMock invocation) {
                if (null != context.username)
                  return new CoyotePrincipal(context.username);
                else
                  return null;
              }
            }
        );
    when(authService.hasAssurance(ASSURANCE_WE_HAVE))
        .thenReturn(true);
    when(authService.hasAssurance(ASSURANCE_WE_DONT_HAVE))
        .thenReturn(false);
    when(authService.hasAssurance(ANOTHER_ASSURANCE_WE_HAVE))
        .thenReturn(true);

  }
}
