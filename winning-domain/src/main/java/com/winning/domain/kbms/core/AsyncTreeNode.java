package com.winning.domain.kbms.core;

import java.util.List;

public class AsyncTreeNode extends BaseTreeNode
{
    private static final long serialVersionUID = 1L;

    private List <BaseTreeNode> children;

    public List <BaseTreeNode> getChildren ()
    {
        return children;
    }

    public void setChildren (List <BaseTreeNode> children)
    {
        this.children = children;
    }
}
