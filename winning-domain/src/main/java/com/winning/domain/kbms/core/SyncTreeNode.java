package com.winning.domain.kbms.core;

import java.util.ArrayList;
import java.util.List;

import com.winning.annotations.mybatis.Exclude;

public class SyncTreeNode extends BaseTreeNode
{
    private static final long serialVersionUID = 1L;

    private List <BaseTreeNode> children = new ArrayList <BaseTreeNode> (0);

    @Exclude
    public List <BaseTreeNode> getChildren ()
    {
        return children;
    }

    public void setChildren (List <BaseTreeNode> children)
    {
        this.children = children;
    }
}
