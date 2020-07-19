package rocks.turncodr.mycurriculum.error;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


/**
 * Handler for secured pages.
 */
@Component
public class LoginAccessDeniedHandler implements AccessDeniedHandler {
  private final Logger log = LoggerFactory.getLogger(LoginAccessDeniedHandler.class);

  @Override
  public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                     AccessDeniedException e) throws IOException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth != null) {
      log.info(auth.getName()
                    + " was trying to access protected resource: \n"
                    + httpServletRequest.getRequestURI() + "\n");
    }
    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/access-denied");

  }
}
