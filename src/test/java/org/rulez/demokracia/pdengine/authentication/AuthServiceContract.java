package org.rulez.demokracia.pdengine.authentication;

import static org.mockito.Mockito.*;

import org.apache.catalina.connector.CoyotePrincipal;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rulez.demokracia.pdengine.Context;

public interface AuthServiceContract {

  String AUTHENTICATED_USER_NAME = "authenticateduser";
  String USER = "user";
  String OTHER_USER = "other user";

  String ASSURANCE_WE_DONT_HAVE = "dontHave";
  String ASSURANCE_WE_HAVE = "have";
  String ANOTHER_ASSURANCE_WE_HAVE = "haveToo";

  default void contract(
      final AuthenticatedUserService authService,
      final Context context
  ) {
    doReturn(context.username)
        .when(authService)
        .getAuthenticatedUserName();
    doAnswer(
        new Answer<CoyotePrincipal>() {

          @Override
          public CoyotePrincipal answer(final InvocationOnMock invocation) {
            if (null == context.username)
              return null;
            else
              return new CoyotePrincipal(context.username);
          }
        }
    )
        .when(authService)
        .getUserPrincipal();
    doReturn(true)
        .when(authService)
        .hasAssurance(ASSURANCE_WE_HAVE);
    doReturn(false)
        .when(authService)
        .hasAssurance(ASSURANCE_WE_DONT_HAVE);
    doReturn(true)
        .when(authService)
        .hasAssurance(ANOTHER_ASSURANCE_WE_HAVE);

  }
}
