package com.gioov.oryx.common.easyui;

import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2019-05-04
 */
public class TreeGrid {

    private Long id;
    private String name;
    private Long parentId;
    private List<TreeGrid> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<TreeGrid> getChildren() {
        return children;
    }

    public void setChildren(List<TreeGrid> children) {
        this.children = children;
    }
}
