package win.scolia.sso.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/account/register")
public class RegisterFilter implements Filter {

    @Value("${sso.register.enable}")
    private boolean enableRegister;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RegisterFilter init...");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!enableRegister) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendError(501, "Register service close");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RegisterFilter destroy...");
        }
    }
}
