package win.scolia.cloud.sso.bean.entity;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1367975609955428597L;

    private Date createTime;

    private Date lastModified;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "createTime=" + createTime +
                ", lastModified=" + lastModified +
                '}';
    }

    /**
     * 当这个对象是为了创建插入记录时创建的, 自动设置两个时间
     */
    public void forCreate() {
        this.createTime = new Date();
        this.lastModified = new Date();
    }

    /**
     * 当这个对象是为了创建更新记录时创建的, 自动设置最后修改时间
     */
    public void forUpdate() {
        this.lastModified = new Date();
    }
}
