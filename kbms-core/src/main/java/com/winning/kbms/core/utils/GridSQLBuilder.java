package com.winning.kbms.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class GridSQLBuilder
{
	/**
	 * eq : 等于 ne ：不等于 lt ：小于 le ：小于等于 gt ：大于 ge ：大于等于 nu ：is null nn : is not
	 * null in : 属于 ni ：不属于 bw : 开始于 bn : 不开始于 ew : 结束于 en ：不结束于 cn : 包含 nc :
	 * 不包含
	 * 
	 */
	public static Map<String, String> OPERATORS = new HashMap<String, String>();

	/**
	 * 查询操作符
	 */
	public static String SEARCHOPER = "searchOper";

	/**
	 * 查询关键字
	 */
	public static String SEARCHSTRING = "searchString";

	/**
	 * 查询字段
	 */
	public static String SEARCHFIELD = "searchField";

	/**
	 * 组合查询参数名
	 */
	public static String FILTERS = "filters";

	/**
	 * SQL片段
	 */
	public static String SQLFRAGMENT = "SqlFragment";

	static
	{
		OPERATORS.put("eq", "=");
		OPERATORS.put("ne", "<>");
		OPERATORS.put("lt", "<");
		OPERATORS.put("le", "<=");
		OPERATORS.put("gt", ">");
		OPERATORS.put("ge", ">=");
		OPERATORS.put("nu", "is null");
		OPERATORS.put("nn", "is not null");
		OPERATORS.put("in", "in");
		OPERATORS.put("ni", "not in");
		OPERATORS.put("bw", "");
		OPERATORS.put("bn", "like");
		OPERATORS.put("ew", "");
		OPERATORS.put("en", "like");
		OPERATORS.put("cn", "like");
		OPERATORS.put("nc", "not like");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> buildSqlFragmentAndParams(Map map)
	{
		StringBuffer sb = new StringBuffer();
		if (map.containsKey(SEARCHFIELD) && StringUtils.isNotEmpty((String) map.get(SEARCHFIELD))
				&& map.containsKey(SEARCHOPER) && StringUtils.isNotEmpty((String) map.get(SEARCHOPER))
				&& map.containsKey(SEARCHSTRING) && StringUtils.isNotEmpty((String) map.get(SEARCHSTRING)))
		{
			// 简单查询条件
			sb.append(map.get(SEARCHFIELD)).append(" ").append(OPERATORS.get(map.get(SEARCHOPER))).append(" #{")
					.append(SEARCHSTRING).append("}");

			map.put(SQLFRAGMENT, sb.toString());
			map.put(SEARCHSTRING, wrapParam((String) map.get(SEARCHOPER), map.get(SEARCHSTRING)));
		}
		else
		{
			// 复合查询
			/*
			 * {"groupOp":"OR", "rules":[
			 * {"field":"b.pro_code","op":"eq","data":"ddd"},
			 * {"field":"b.pro_name","op":"cn","data":"uuuu"},
			 * {"field":"a.display_name","op":"eq","data":"sss"} ]}
			 */
			if (map.containsKey(FILTERS) && StringUtils.isNotEmpty((String) map.get(FILTERS)))
			{
				Map<String, Object> jsonMap = JackJson.fromJsonToObject((String) map.get(FILTERS), Map.class);
				List<Map<String, String>> rules = (List<Map<String, String>>) jsonMap.get("rules");
				for (int i = 0; i < rules.size(); i++)
				{
					Map<String, String> rule = rules.get(i);

					// 拼接SQL
					sb.append(rule.get("field")).append(" ").append(OPERATORS.get(rule.get("op"))).append(" #{field")
							.append(i).append("} ");
					if (i + 1 < rules.size())
					{
						sb.append(jsonMap.get("groupOp")).append(" ");
					}

					// 组装参数
					map.put("field" + i, wrapParam(rule.get("op"), rule.get("data")));
				}

				map.put(SQLFRAGMENT, sb.toString());
			}
		}

		return map;
	}
	
	/**
	 * 包装值对象
	 * 
	 * @param op
	 * @param value
	 * @return
	 */
	protected static Object wrapParam(String op, Object value)
	{
		if ("cn".equals(op) || "nc".equals(op))
		{
			value = "%" + value + "%";
		}
		else if ("bn".equals(op))
		{
			value = value + "%";
		}
		else if ("en".equals(op))
		{
			value = "%" + value;
		}
		return value;
	}

	// public static void main(String[] args)
	// {
	// String json =
	// "{\"groupOp\":\"OR\", \"rules\":[{\"field\":\"b.pro_code\",\"op\":\"eq\",\"data\":\"ddd\"},{\"field\":\"b.pro_name\",\"op\":\"cn\",\"data\":\"uuuu\"},{\"field\":\"a.display_name\",\"op\":\"eq\",\"data\":\"sss\"} ]}";
	// }
}
