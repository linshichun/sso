package win.scolia.cloud.sso.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.autoconfigure.SSOProperties;

import java.util.List;

@Component
public class PageUtils {

    @Autowired
    private SSOProperties properties;

    public void startPage(int pageNum) {
        PageHelper.startPage(pageNum, properties.getPage().getSize());
    }

    public <T> PageInfo<T> getPageInfo(List<T> content) {
        return new PageInfo<>(content);
    }

}
