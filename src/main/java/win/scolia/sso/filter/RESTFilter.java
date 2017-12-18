package win.scolia.sso.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * 支持put和delete方法提交表单
 */
@Component
public class RESTFilter extends HiddenHttpMethodFilter {
}