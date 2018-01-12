package win.scolia.cloud.sso.bean.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1367975609955428597L;

    private Date createTime;

    private Date lastModified;

    /**
     * 当这个对象是为了创建插入记录时创建的, 自动设置两个时间
     */
    @SuppressWarnings("unchecked")
    public <T> T forCreate() {
        this.createTime = new Date();
        this.lastModified = new Date();
        return (T) this;
    }

    /**
     * 当这个对象是为了创建更新记录时创建的, 自动设置最后修改时间
     */
    @SuppressWarnings("unchecked")
    public <T> T forUpdate() {
        this.lastModified = new Date();
        return (T) this;
    }
}
