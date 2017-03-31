package com.winning.kbms.core.service.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import com.winning.annotations.kbms.clinical.IncludeLog;
import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Id;
import com.winning.annotations.mybatis.Table;
import com.winning.domain.kbms.core.ModifyDomain;
import com.winning.kbms.core.dao.BaseDao;
import com.winning.kbms.core.domain.ExportDefine;
import com.winning.kbms.core.domain.Page;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.service.PagingService;
import com.winning.kbms.core.service.QueryService;
import com.winning.kbms.core.utils.GridSQLBuilder;
import com.winning.mybatis.support.Criteria;
import com.winning.utils.GenericsUtils;
import com.winning.utils.MapUtils;


public abstract class AbstractQueryService<T extends ModifyDomain>
    implements QueryService<T>, PagingService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("kbmsBaseDao")
    private BaseDao baseDao;

    private Class<T> domainClass;


    protected BaseDao getDao() {
        return baseDao;
    }


    /**
     * 获取查询语句
     * 
     * @param params
     * @return
     * @throws Exception
     */
    protected String getSearchSql(Map<String, Object> params) throws Exception {
        Class<T> clazz = this.getDomainClass();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            if (StringUtils.isEmpty(table.selectSql())) {
                StringBuilder sb = new StringBuilder();
                sb.append("select * from ").append(table.value());
                return sb.toString();
            }
            else {
                return table.selectSql();
            }
        }
        else {
            throw new ServiceException(clazz.getName() + " 没有添加  Table 注解");
        }
    }


    /**
     * 获取总数查询语句
     * 
     * @param params
     * @return
     * @throws Exception
     */
    protected String getCountSql(Map<String, Object> params) throws Exception {
        Class<T> clazz = this.getDomainClass();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            if (StringUtils.isEmpty(table.selectSql())) {
                StringBuilder sb = new StringBuilder();
                sb.append("select count(1) from ").append(table.value());
                return sb.toString();
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append("select count(1) from (").append(table.selectSql())
                        .append(")");
                return sb.toString();
            }
        }
        else {
            throw new ServiceException(clazz.getName() + " 没有添加  Table 注解");
        }
    }


    /**
     * 获取提交sql语句
     * 
     * @param params
     * @return
     * @throws Exception
     */
    protected String getSubmitSql(T baseDomain) throws Exception {
        Class<T> clazz = this.getDomainClass();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            StringBuilder sb = new StringBuilder();
            sb.append("update ")
                    .append(table.value())
                    .append(" set is_submit = '1'  where id = #{id,jdbcType = VARCHAR}");
            return sb.toString();
        }
        else {
            throw new ServiceException(clazz.getName() + " 没有添加  Table 注解");
        }
    }


    @Override
    @SuppressWarnings({
        "rawtypes"
    })
    @Transactional(readOnly = true)
    public Page<?> findOnPage(Map<String, Object> params, Page page)
        throws Exception {
        return findOnPage(getCountSql(params), getSearchSql(params), params,
                page);
    }


    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    protected Page<?> findOnPage(String countSql, String searchSql,
        Map<String, Object> params, Page page) {
        GridSQLBuilder.buildSqlFragmentAndParams(params);

        int totalCount = 0;
        if (StringUtils.isNotEmpty(countSql)) {
            countSql = addConditions(countSql, params);

            // 查询数据库条数
            totalCount = getDao().countBySql(countSql, params);
            page.setTotalCount(totalCount);

            // 如果查询的条数为0,则无需再查询数据，直接返回
            if (totalCount == 0)
                return page;
        }

        // 查询数据
        StringBuilder searchSqlBuilder =
                new StringBuilder(addConditions(searchSql, params));
        if (StringUtils.isNotEmpty(page.getOrderBy())) {
            if (StringUtils.isNotEmpty(page.getOrder()))
                searchSqlBuilder.append(" order by ").append(page.getOrderBy())
                        .append(" ").append(page.getOrder()).append(",1,2");
            else
                searchSqlBuilder.append(" order by ").append(page.getOrderBy())
                        .append(" asc ").append(",1,2");
        }

        if (logger.isDebugEnabled())
            logger.debug(searchSqlBuilder.toString());

        page.setResult(getDao()
                .queryBySql(searchSqlBuilder.toString(), params,
                        page.getPageSize() * (page.getPageNo() - 1),
                        page.getPageSize()));

        return page;
    }


    protected String addConditions(String sql, Map<String, Object> params) {
        StringBuilder sqlBuilder = new StringBuilder(sql);
        String sqlFragment = (String) params.get(GridSQLBuilder.SQLFRAGMENT);
        if (StringUtils.isNotEmpty(sqlFragment)) {
            sqlBuilder.append(" where ").append(sqlFragment);
        }
        return sqlBuilder.toString();
    }


    public List<ExportDefine> getExportDefines(Map<String, Object> params) {
        return null;
    }


    @Transactional(readOnly = true)
    public List<Map<String, Object>> findExportData(Map<String, Object> params,
        int start, int limit, List<ExportDefine> exportDefines)
        throws Exception {
        GridSQLBuilder.buildSqlFragmentAndParams(params);
        String exportDataSql = addConditions(getExportDataSql(params), params);
        return getDao().queryBySql(exportDataSql, params, limit * (start - 1),
                limit);
    }


    public String getExportDataSql(Map<String, Object> params) throws Exception {
        return this.getSearchSql(params);
    }


    public T getById(Serializable id) {
        return (T) getDao().getById(id, getDomainClass());
    }


    public List<T> getAll() {
        return getDao().getAll(getDomainClass());
    }


    public int countByCriteria(Criteria criteria) {
        return getDao().countByCriteria(criteria, getDomainClass());
    }


    @SuppressWarnings("unchecked")
    protected Class<T> getDomainClass() {
        if (domainClass == null) {
            domainClass =
                    GenericsUtils.getSuperClassGenricType(this.getClass());
        }
        return domainClass;
    }


    public List<T> queryByCriteria(Criteria criteria) {
        return getDao().queryByCriteria(criteria, getDomainClass());
    }


    public T queryOneByCriteria(Criteria criteria) {
        return getDao().queryOneByCriteria(criteria, getDomainClass());
    }


    @SuppressWarnings("unchecked")
    public void submit(T baseDomain) {
        try {
            getDao().submit(baseDomain.getClass(),
                    this.getSubmitSql(baseDomain),
                    MapUtils.newMap("id", baseDomain.getId()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    public void logicDelete(T baseDomain) {
        try {
            getDao().logicDelete(baseDomain.getClass(),this.getLogicDeleteSql(baseDomain),
                    MapUtils.newMap("id", baseDomain.getId()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取逻辑删除sql语句
     * 
     * @param params
     * @return
     * @throws Exception
     */
    protected String getLogicDeleteSql(T baseDomain) throws Exception {
        Class<T> clazz = this.getDomainClass();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            StringBuilder sb = new StringBuilder();
            sb.append("update ")
                    .append(table.value())
                    .append(" set is_enable = '0',is_submit = '0' where id = #{id,jdbcType = VARCHAR}");
            return sb.toString();
        }
        else {
            throw new ServiceException(clazz.getName() + " 没有添加  Table 注解");
        }
    }
    
    //查询已存在的ids
    public String[] queryDuplicate(T obj) throws Exception{
        Class<T> clazz = this.getDomainClass();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            Method[] methods = clazz.getMethods();
            StringBuilder sb = new StringBuilder();
            sb.append("select * from ").append(table.value()).append(" where 1=1 ");
            for (Method method : methods) {
                if (method.getName().startsWith("get")) {
                    IncludeLog includeLog =method.getAnnotation(IncludeLog.class);
                    if(null!=includeLog&&includeLog.required()){
                        Method m = obj.getClass().getMethod(method.getName());
                        String value = null!= m.invoke(obj)? m.invoke(obj).toString(): null;
                        String column = null;
                        Column columnAnno = method.getAnnotation(Column.class);
                        if(columnAnno == null){
                        	column = method.getAnnotation(Id.class).value();
                        }else{
                        	column = columnAnno.value();
                        }
                        
                        if(StringUtils.isEmpty(value)){
                            sb.append(" and ( ").append(column).append(" = '' or ").append(column).append(" is null )");
                        }else {
                            sb.append(" and ").append(column).append(" = '").append(value).append("' ");
                        }
                    }
                }
            }
            sb.append(" and is_enable = 0 ");
            List<String> listID =new ArrayList<String>();
            List<Map<Object, Object>> list = getDao().queryBySql(sb.toString());
            if(CollectionUtils.isNotEmpty(list)){
                for(Map<Object, Object> map : list){
                    listID.add(String.valueOf(map.get("ID")));
                }
            }
            return (String[])listID.toArray(new String[listID.size()]);
        }
        else {
            throw new ServiceException(clazz.getName() + " 没有添加  Table 注解");
        }
    }
    //恢复数据
    @SuppressWarnings("unchecked")
    public void logicReduction(T baseDomain) {
        try {
            getDao().logicReduction(baseDomain.getClass(),this.getLogicReductionSql(baseDomain),
                    MapUtils.newMap("id", baseDomain.getId()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取逻辑恢复sql语句
     * 
     * @param params
     * @return
     * @throws Exception
     */
    protected String getLogicReductionSql(T baseDomain) throws Exception {
        Class<T> clazz = this.getDomainClass();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            StringBuilder sb = new StringBuilder();
            sb.append("update ")
                    .append(table.value())
                    .append(" set is_enable = '1' , is_submit = '0' where id = #{id,jdbcType = VARCHAR}");
            return sb.toString();
        }
        else {
            throw new ServiceException(clazz.getName() + " 没有添加  Table 注解");
        }
    }
}
