package com.winning.kbms.dao.impl;

import java.io.Serializable;
import java.util.Map;

import com.winning.kbms.core.dao.impl.BaseDaoImpl;

/**
 * 
 * <p>Class类功能定义的说明性内容。（请以句号“。”结尾、段中换行请使用“<br/>”符号）</p> 
 * <p>说明: </p> 
 * <p>备注: </p> 
 * 
 * @version 1.0
 * @author 公司名 : 上海金仕达卫宁软件科技有限公司（Shanghai KingStar WinningSoft LTD.） <br />
 * 变更履历 <br />
 *
 */
public class BaseWithLogDaoImpl extends BaseDaoImpl {

	@Override
	public <T> void add(T obj) {
		super.add(obj);
	}

	@Override
	public <T> void update(T obj) {
		super.update(obj);
	}

	@Override
	public <T> void deleteById(Serializable id, Class<T> clazz) {
		super.deleteById(id, clazz);
	}
	
	@Override
	public <T> void submit(Class<?> clazz,String sql ,Map<String, Object> _params){
		super.submit( clazz, sql, _params);
	}
    
    @Override
    public <T> void logicDelete(Class<T> clazz, String _sql, Map<String, Object> _params) {
        super.logicDelete(clazz,_sql, _params);
    }
    
    @Override
    public <T> void logicReduction(Class<T> clazz, String _sql, Map<String, Object> _params){
        super.logicReduction(clazz,_sql, _params);;
    }
	
}
