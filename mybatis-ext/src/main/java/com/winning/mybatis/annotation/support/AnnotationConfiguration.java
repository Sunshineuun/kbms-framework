package com.winning.mybatis.annotation.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.ibatis.session.Configuration;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Exclude;
import com.winning.annotations.mybatis.Id;
import com.winning.annotations.mybatis.Table;
import com.winning.mybatis.support.ClassMap;
import com.winning.mybatis.support.ClassMap.Property;
import com.winning.utils.ReflectionUtils;

public final class AnnotationConfiguration {
	private final Configuration configuration;
	private final static Map<Class<?>, ClassMap> classMaps = new ConcurrentHashMap<Class<?>, ClassMap>();

	public AnnotationConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void addAnnotatedClass(Class<?> clazz) throws Exception {
		parseClassMap(clazz);
	}

	private void parseClassMap(Class<?> clazz) throws Exception {
		Table table = clazz.getAnnotation(Table.class);

		List<Field> fields = ReflectionUtils.getFields(clazz);

		String readMethodName;
		String writeMethodName;
		Column column;
		Id id;
		ClassMap classMap = new ClassMap(clazz, table.value(), table.resultMapId(), table.selectSql());
		for (Field field : fields) {
			column = null;
			id = null;

			readMethodName = getReadMethodName(field.getName(), field.getType());
			Method readMethod = MethodUtils.getAccessibleMethod(clazz, readMethodName, new Class[] {});
			if (readMethod == null)
				continue;

			if (readMethod.isAnnotationPresent(Exclude.class))
				continue;

			if (readMethod.isAnnotationPresent(Column.class))
				column = readMethod.getAnnotation(Column.class);

			if (readMethod.isAnnotationPresent(Id.class))
				id = readMethod.getAnnotation(Id.class);

			writeMethodName = getWriteMethodName(field.getName());
			Method writeMethod = MethodUtils.getAccessibleMethod(clazz, writeMethodName,
					new Class[] { field.getType() });

			if (writeMethod == null)
				continue;

			if (writeMethod.isAnnotationPresent(Exclude.class))
				continue;

			if (writeMethod.isAnnotationPresent(Column.class))
				column = writeMethod.getAnnotation(Column.class);

			if (writeMethod.isAnnotationPresent(Id.class))
				id = writeMethod.getAnnotation(Id.class);

			if (column == null && id == null) {
				Property property = classMap.new Property(field.getName(), field.getType(), field.getName()
						.toUpperCase(), true, true);
				classMap.addProperty(property);
				continue;
			}

			if (id != null) {
				Property property = classMap.new Property(field.getName(), field.getType(), id.value().toUpperCase(),
						true, true);
				classMap.addIdProperty(property);
				continue;
			}

			if (column != null) {
				Property property = classMap.new Property(field.getName(), field.getType(), column.value()
						.toUpperCase(), column.isAdd(), column.isUpdate());
				classMap.addProperty(property);
				continue;
			}
		}
		classMaps.put(clazz, classMap);
	}

	public static Map<Class<?>, ClassMap> getClassMaps() {
		return classMaps;
	}

	public List<ClassMap> getClassMapList() {
		List<ClassMap> list = new ArrayList<ClassMap>(classMaps.values());
		return list;
	}

	public static ClassMap getClassMap(Class<?> clazz) {
		return classMaps.get(clazz);
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	private String getReadMethodName(String propertyName, Class<?> type) {
		char c1 = propertyName.charAt(0);
		char c2 = propertyName.charAt(1);

		StringBuilder sb = new StringBuilder();
		if (type == boolean.class || type == Boolean.class) {
			sb.append("is");
		} else {
			sb.append("get");
		}
		if (c2 >= 'A' && c2 <= 'Z')
			sb.append(c1).append(propertyName.substring(1));
		else
			sb.append(Character.toUpperCase(c1)).append(propertyName.substring(1));
		return sb.toString();
	}

	private String getWriteMethodName(String propertyName) {
		char c1 = propertyName.charAt(0);
		char c2 = propertyName.charAt(1);

		StringBuilder sb = new StringBuilder();

		if (c2 >= 'A' && c2 <= 'Z')
			sb.append("set").append(c1).append(propertyName.substring(1));
		else
			sb.append("set").append(Character.toUpperCase(c1)).append(propertyName.substring(1));
		return sb.toString();
	}
}
