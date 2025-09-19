package net.linlan.emis.support.manage.node;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 节点工具类
 *
 * @author Linlan
 */
public class NodeUtils implements Serializable {
    private static final long serialVersionUID = -8631010419770301106L;

    /**
     * 获取树
     *
     * @param list list
     * @param <T>  类型
     * @return  树的节点
     */
    public static <T extends BaseNode> List<T> getNodeList(List<T> list) {
        for (T li : list) {
            List<T> ts = Lists.newArrayList();
            //设置子菜单
            for (T entity : list) {
                //子节点和父节点不一致
                if (!li.getId().equals(entity.getId())
                    //父节点等于子节点
                    && li.getId().equals(entity.getParentId())) {
                    ts.add(entity);
                    li.setChildren(ts);
                    //如果已经成为了子节点,设置状态
                    entity.setLeaf(true);
                }
            }
        }
        list.removeIf(T::isLeaf);
        return list;
    }
}
