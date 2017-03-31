package com.winning.kbms.core.service;

import java.util.List;
import java.util.Map;

import com.winning.domain.kbms.core.BaseTreeNode;

public interface TreeGridService <T extends BaseTreeNode> extends TreeService <T>
{
    public List <T> searchTree (Map <String, Object> params);
}
