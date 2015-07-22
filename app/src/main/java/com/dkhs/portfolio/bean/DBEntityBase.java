/**
 * @Title EntityBase.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午2:31:19
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.lidroid.xutils.db.annotation.NoAutoIncrement;

/**
 * @author zjz
 * @version 1.0
 * @ClassName EntityBase
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-17 下午2:31:19
 */
public abstract class DBEntityBase {
    // @Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    @NoAutoIncrement
    // int,long类型的id默认自增，不想使用自增时添加此注解
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
