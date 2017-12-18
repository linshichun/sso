package win.scolia.sso.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.HttpPutFormContentFilter;

/**
 * 支持put和delete方法提交表单
 */
@Component
public class PutFilter extends HttpPutFormContentFilter {
}