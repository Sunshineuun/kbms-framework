package com.winning.kbms.core.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.winning.domain.kbms.core.ModifyDomain;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.mybatis.support.Criteria;


public interface ManagementService<T extends ModifyDomain>
    extends QueryService<T> {
    /**
     * 新增
     * 
     * @param baseDomain
     * @return
     * @throws ServiceException
     */
    public void add(T baseDomain) throws ServiceException;


    /**
     * 修改
     * 
     * @param baseDomain
     * @return
     * @throws ServiceException
     */
    public void update(T baseDomain) throws ServiceException;


    // TODO
    /**
     * 提交
     * 
     * @param baseDomain
     * @return
     * @throws ServiceException
     */
    public void submit(T baseDomain) throws ServiceException;


    public void deleteById(String id) throws ServiceException;


    public void deleteByCriteria(Criteria criteria) throws ServiceException;


    public void submit(List<T> baseDomains) throws ServiceException;


    public void add(List<T> baseDomains) throws ServiceException;


    public void update(List<T> baseDomains) throws ServiceException;


    public void delete(List<T> baseDomains) throws ServiceException;


    public void logicDelete(T baseDomain) throws ServiceException;


    public void logicDelete(List<T> baseDomains) throws ServiceException;
    
    
    public String[] queryDuplicate(T baseDomain) throws Exception;
    
    
    public void logicReduction(List<T> baseDomains) throws ServiceException;
    
    public boolean isKnowledgeReviewing(T obj, HttpServletRequest request, String oper);

}
