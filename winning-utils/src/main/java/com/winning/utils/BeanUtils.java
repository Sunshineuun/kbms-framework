/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: BeanUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import com.winning.utils.cover.DateConverter;

public class BeanUtils {
	// 升序
	private static String ASC_SORT = "+";
	// 降序
	private static String DESC_SORT = "-";
	// 是否允许null
	private static boolean NULL_ALLOW = true;

	/**
	 * @作者: yangjizhou
	 * @时间: 2015-8-6 下午2:46:27
	 * @描述: 
	 *      根据sortFields对传入的sourceList进行排序。例如Test类有id,name,age属性，要根据id升序，name降序排序
	 *      。则参数里的T用Test,sortFields配置为"id+","name-"
	 *      <b>不对传入的集合进行做排序，使用时请使用返回后的集合做业务处理</b>
	 * @param sourceList
	 * @param sortFields
	 * @return
	 * @备注:
	 */
    @SuppressWarnings("unchecked")
    public static <T> List<T> classSort(List<T> sourceList,
			String... sortFields) {
		List<T> result = new ArrayList<T>();
		result.addAll(sourceList);
		if (CollectionUtils.isEmpty(sourceList) || sourceList.contains(null)) {
			return null;
		}
		if (sortFields == null || sortFields.length == 0) {
			return result;
		}
		ArrayList<BeanComparator> cmps = new ArrayList<BeanComparator>();
		for (int i = 0; i < sortFields.length; i++) {
			if (StringUtils.isEmpty(sortFields[i])) {
				continue;
			}
			String str = sortFields[i].trim();
			String sortType = null;
			if (!str.endsWith(ASC_SORT) && !str.endsWith(DESC_SORT)) {
				continue;
			} else if (str.endsWith(ASC_SORT)) {
				sortType = ASC_SORT;
			} else if (str.endsWith(DESC_SORT)) {
				sortType = DESC_SORT;
			}
			int index = str.indexOf(sortType);
			String sortField = str.substring(0, index);
			BeanComparator cmp = getBeanComparator(sortField, sortType);
			if (cmp != null) {
				cmps.add(cmp);
			}
		}
		// 创建一个排序链
		ComparatorChain multiSort = new ComparatorChain(cmps);
		try {
			Collections.sort(result, multiSort);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	private static BeanComparator getBeanComparator(String sortField,
			String sortType) {
		if (StringUtils.isEmpty(sortField) || sortType == null) {
			return null;
		}
		// 创建一个排序规则
		Comparator<?> comparator = ComparableComparator.getInstance();
		if (sortType.equals(DESC_SORT)) {
			comparator = ComparatorUtils.reversedComparator(comparator); // 逆序
		}
		if (NULL_ALLOW) {
			comparator = ComparatorUtils.nullLowComparator(comparator); // 允许null
		}
		return new BeanComparator(sortField, comparator);
	}
	
	/**
	 * @作者: yangjizhou
	 * @时间: 2016-10-13 上午10:46:48 
	 * @描述: 批量将map不区分大小写封装到bean
	 * @param list
	 * @param clazz
	 * @return
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @备注:
	 */
	public static <T> List<T> mapList2BeanList(List<Map<String, Object>> list,Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		if (CollectionUtils.isEmpty(list) || clazz==null) {
			return null;
		}
		List<T> result=new LinkedList<T>();
		for(Map<String, Object> map:list){
			result.add(map2Bean(map, clazz));
		}
		return result;
	}
	
	/**
	 * @作者: yangjizhou
	 * @时间: 2016-10-13 上午10:04:29 
	 * @描述: 将map不区分大小写封装到bean
	 * @param map
	 * @param clazz
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws Exception
	 * @备注:
	 */
	public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException  {
		if (MapUtils.isEmpty(map) || clazz==null) {
			return null;
		}
		//获取类中所有的属性名称（不包括继承的）
		List<String> classPropertyNames = getClassMemberProperty(clazz);
		if (CollectionUtils.isEmpty(classPropertyNames)) {
			return null;
		}
		//将map中的key全转为大写，方便查询
		Map<String, Object> mapUpper=new HashMap<String, Object>();
		for(Map.Entry<String, Object> entry:map.entrySet()){
			if(entry.getKey()!=null){
				mapUpper.put(entry.getKey().toString().toUpperCase(), entry.getValue());
			}
		}
		//实际使用到的map
		Map<String, Object> mapResult=new HashMap<String, Object>();
		T result = clazz.newInstance();
		for (String name : classPropertyNames) {
			String nameUpper=name.toUpperCase();
			if(mapUpper.containsKey(nameUpper)){
				mapResult.put(name, mapUpper.get(nameUpper));
			}
		}
		org.apache.commons.beanutils.BeanUtils.populate(result, mapResult);
		return result;
	}
	
	/**
	 * @作者: yangjizhou
	 * @时间: 2016-10-13 上午10:04:29 
	 * @描述: 返回类T中的所有成员属性名
	 * @param clazz
	 * @return
	 * @备注:
	 */
	public static <T> List<String> getClassMemberProperty(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		List<String> result = new LinkedList<String>();
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return null;
		}
		for (int i = 0; i < fields.length; i++) {
			result.add(fields[i].getName());
		}
		return result;
	}
	
	 /**
	  * @作者: yangjizhou
	  * @时间: 2016-12-23 下午5:49:13 
	  * @描述: 将bean对象映射到map对象
	  * @param obj
	  * @return
	  * @throws IntrospectionException
	  * @throws IllegalArgumentException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @备注:
	  */
	public static Map<String, Object> transBean2Map(Object obj)
			throws IntrospectionException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			// 过滤class属性
			if (!key.equals("class")) {
				// 得到property对应的getter方法
				Method getter = property.getReadMethod();
				Object value = getter.invoke(obj);
				map.put(key, value);
			}
		}
		return map;
	}
	
	/**
	 * @作者: yangjizhou
	 * @时间: 2016-12-23 下午6:27:35 
	 * @描述: 将map对象映射到bean对象
	 * @param map
	 * @param obj
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @备注:
	 */
	public static void transMap2Bean(Map<String, Object> map, Object obj)
			throws IllegalAccessException, InvocationTargetException {
		if (map == null || obj == null) {
			return;
		}
		org.apache.commons.beanutils.BeanUtils.populate(obj, map);
	}
	
	/**
	 * BeanUtils的populate方法之日期处理
	 * @param map
	 * @param obj
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void transMap2BeanWithTime(Map<String, Object> map, Object obj)
			throws IllegalAccessException, InvocationTargetException {
		if (map == null || obj == null) {
			return;
		}
		BeanUtilsBean beanUtilsBean = getBeanUtilsBean();
        beanUtilsBean.populate(obj, map);  
	}
	
	private static BeanUtilsBean getBeanUtilsBean(){
		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		DateConverter dtConverter = new DateConverter();  
        /* 可以注册多个  */ 
		/* 将数据库查出的Timestamp类型转为string */ 
        convertUtils.register(dtConverter, String.class);
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtils,  
            new PropertyUtilsBean());  
        return beanUtilsBean;
	}
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */