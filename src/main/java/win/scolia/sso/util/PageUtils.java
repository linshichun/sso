package win.scolia.sso.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageUtils {

    @Value("${scolia.sso.page.size}")
    private Integer pageSize;

    public void startPage(int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
    }

    public <T> PageInfo<T> getPageInfo(List<T> content) {
        return new PageInfo<>(content);
    }

}
