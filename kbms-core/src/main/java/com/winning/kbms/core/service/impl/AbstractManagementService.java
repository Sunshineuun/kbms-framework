package com.winning.kbms.core.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;

import com.winning.domain.kbms.core.ModifyDomain;
import com.winning.kbms.core.commons.Constants;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.service.ManagementService;
import com.winning.mybatis.support.Criteria;
import com.winning.utils.MapUtils;

public abstract class AbstractManagementService<T extends ModifyDomain> extends AbstractQueryService<T> implements
		ManagementService<T> {
	@Override
	@Transactional
	public void add(T baseDomain) throws ServiceException {
		this.getDao().add(baseDomain);
	}

	@Override
	@Transactional
	public void update(T baseDomain) throws ServiceException {
		this.getDao().update(baseDomain);
	}

	@Override
	@Transactional
	public void submit(T baseDomain) throws ServiceException {
		super.submit(baseDomain);
	}
	
	@Override
	@Transactional
	public void deleteById(String id) throws ServiceException {
		this.getDao().deleteById(id, getDomainClass());
	}


	@Override
	@Transactional
	public void deleteByCriteria(Criteria criteria) throws ServiceException {
		this.getDao().deleteByCriteria(criteria, getDomainClass());
	}

	@Override
	@Transactional
	public void add(List<T> baseDomains) throws ServiceException {
		for (int i = 0; i < baseDomains.size(); i++) {
			this.add(baseDomains.get(i));
		}
	}

	@Override
	@Transactional
	public void update(List<T> baseDomains) throws ServiceException {
		for (int i = 0; i < baseDomains.size(); i++) {
			this.update(baseDomains.get(i));
		}
	}

	@Override
	@Transactional
	public void submit(List<T> baseDomains) throws ServiceException {
		for (int i = 0; i < baseDomains.size(); i++) {
			this.submit(baseDomains.get(i));
		}
	}
	
	@Override
	@Transactional
	public void delete(List<T> baseDomains) throws ServiceException {
		for (int i = 0; i < baseDomains.size(); i++) {
			this.deleteById(baseDomains.get(i).getId());
		}
	}
	
    @Override
    @Transactional
    public void logicDelete(List<T> baseDomains) throws ServiceException {
        for (int i = 0; i < baseDomains.size(); i++) {
            logicDelete(baseDomains.get(i));
        }
    }
    
    @Override
    @Transactional
    public void logicDelete(T baseDomain) throws ServiceException {
        super.logicDelete(baseDomain);
    }
    
    public String[] queryDuplicate(T baseDomain)  throws Exception{
       return super.queryDuplicate(baseDomain); 
    }
    
    @Override
    @Transactional
    public void logicReduction(List<T> baseDomains) throws ServiceException {
        for (int i = 0; i < baseDomains.size(); i++) {
            logicReduction(baseDomains.get(i));
        }
    }
    @Override
    @Transactional
    public void logicReduction(T baseDomain) throws ServiceException {
        super.logicReduction(baseDomain);
    }
    
    @Override
    public boolean isKnowledgeReviewing(T obj, HttpServletRequest request,
    		String oper) {
    	String sql = "SELECT COUNT(1) FROM KBMS_KNOWLEDGE_REVIEW WHERE TARGET_ID = #{id,jdbcType=VARCHAR} AND TARGET_CLASS_NAME = #{targetClassName,jdbcType=VARCHAR} AND IS_REVIEWING = 0 ";
    	String id = null;
    	if(oper.equals(Constants.OPER_EDIT)){
    		id = obj.getId();
    	}else if(oper.equals(Constants.OPER_LOGIC_DEL)
    			|| oper.equals(Constants.OPER_DEL)){
    		id = request.getParameter("id");
    	}else if(oper.equals(Constants.OPER_ADD)){
    		return false;
    	}
    	@SuppressWarnings("unchecked")
		Integer count = super.getDao().countBySql(sql, MapUtils.newMap("id",id,"targetClassName",obj.getClass().getSimpleName()));
    	if(count == 1){
    		try{
    			if(count >= 2){
        			throw new Exception("同一知识未审核数据出现大于两次！");
        		}
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		return true;
    	}
    	return false;
    }
}
