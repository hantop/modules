package com.fenlibao.common.pms.util.tree;

import java.util.List;

/**
 * Created by Lullaby on 2015-11-06 16:07
 */
public class Node {

    private String id;

    private String parentId;

    private List<Node> children;

    public Node() {

    }

    public Node(String id, String parentId) {
        super();
        this.id = id;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

}
