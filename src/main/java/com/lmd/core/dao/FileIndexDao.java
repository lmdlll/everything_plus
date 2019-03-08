package com.lmd.core.dao;

import com.lmd.core.Model.Condition;
import com.lmd.core.Model.Thing;

import java.util.List;


/**该接口实现：
 * 业务层访问数据库的CRUD
 * 本来可以将检索与索引分开写功能实现，但是代码重复率高(都要连接数据库)。因此，将其单独拿出来变成一个接口，让其子类实现
 */
public interface FileIndexDao {
    /**
     * 插入数据
     * @param thing
     */
    void insert(Thing thing);

    /**
     * 删除数据Thing
     * @param thing
     */
    void delete(Thing thing);

    /**
     * 根据condition条件进行数据库的检索
     * @param condition
     * @return
     */
    List<Thing> search(Condition condition);


}
