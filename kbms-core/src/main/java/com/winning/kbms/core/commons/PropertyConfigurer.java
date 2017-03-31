package com.winning.kbms.core.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 通过spring的参数配置，将所有参数配置包装在一个map里面
 * 
 * @author gang.liu
 * @date Nov 20, 2013
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer
{
	private static Map<String, Object> ctxPropertiesMap;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException
	{
		super.processProperties(beanFactoryToProcess, props);
		ctxPropertiesMap = new HashMap<String, Object>();
		for (Object key : props.keySet())
		{
			String keyStr = (String) key;
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
	}

	public static String getString(String name)
	{
		Object obj = ctxPropertiesMap.get(name);
		if (obj == null)
			return null;
		
		if (obj instanceof String)
			return (String) obj;

		return obj.toString();
	}

	public static int getInt(String name)
	{
		Object obj = ctxPropertiesMap.get(name);

		if (obj == null || "".equals(obj))
			return 0;

		if (obj instanceof Integer)
			return (Integer) obj;

		return Integer.parseInt(obj.toString());
	}

	public static boolean getBoolean(String name)
	{
		Object obj = ctxPropertiesMap.get(name);

		if (obj == null || "".equals(obj))
			return false;

		if (obj instanceof Boolean)
			return (Boolean) obj;

		return Boolean.parseBoolean(obj.toString());
	}
}
