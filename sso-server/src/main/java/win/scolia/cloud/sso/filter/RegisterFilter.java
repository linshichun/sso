package win.scolia.cloud.sso.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import win.scolia.cloud.sso.autoconfigure.SSOProperties;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/account/register")
public class RegisterFilter implements Filter {

    @Autowired
    private SSOProperties properties;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RegisterFilter init...");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!properties.getRegister().isEnable()) {
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
