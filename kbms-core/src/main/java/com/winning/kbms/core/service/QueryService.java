package com.winning.kbms.core.service;

import java.io.Serializable;
import java.util.List;

import com.winning.domain.kbms.core.ModifyDomain;
import com.winning.mybatis.support.Criteria;

public interface QueryService <T extends ModifyDomain>
{
    public T getById (Serializable id);

    public List <T> getAll ();

    public List <T> queryByCriteria (Criteria criteria);
    
    public T queryOneByCriteria (Criteria criteria);
    
    public int countByCriteria (Criteria criteria);
}
