package net.linlan.emis.support.manage.node;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 基础树
 *
 * @author Linlan
 */
@Data
public class BaseNode implements Serializable {
    private static final long        serialVersionUID = -8631010419770301105L;
    /**
     * ID
     */
    private String                   id;
    /**
     * 父ID
     */
    private String                   parentId;
    /**
     * 是否子叶节点
     */
    private boolean                  leaf;
    /**
     * 子节点
     */
    private List<? extends BaseNode> children;
}
