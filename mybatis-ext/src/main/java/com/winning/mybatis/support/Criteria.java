package com.winning.mybatis.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public final class Criteria {
	private final List<Condition> conditions = new ArrayList<Condition>(5);
	private final List<Order> orders = new ArrayList<Order>(1);

	private Criteria() {
	}

	public static Criteria create() {
		return new Criteria();
	}

	public Criteria andEqualTo(String name, Object value) {
		conditions.add(new Condition("and", name, "=", value));
		return this;
	}

	public Criteria andNotEqualTo(String name, Object value) {
		conditions.add(new Condition("and", name, "<>", value));
		return this;
	}

	public Criteria andThanOrEqualTo(String name, Object value) {
		conditions.add(new Condition("and", name, ">=", value));
		return this;
	}

	public Criteria andLessOrEqualTo(String name, Object value) {
		conditions.add(new Condition("and", name, "<=", value));
		return this;
	}

	public Criteria andLike(String name, Object value) {
		conditions.add(new Condition("and", name, "like", "%" + value + "%"));
		return this;
	}

	public Criteria andNotLike(String name, Object value) {
		conditions.add(new Condition("and", name, "not like", "%" + value + "%"));
		return this;
	}

	public Criteria andLeftLike(String name, Object value) {
		conditions.add(new Condition("and", name, "like", "%" + value));
		return this;
	}
	
	public Criteria andIsNull(String name) {
		conditions.add(new Condition("and", name, "is null"));
		return this;
	}

	public Criteria andIsNotNull(String name) {
		conditions.add(new Condition("and", name, "is not null"));
		return this;
	}

	public Criteria andRightLike(String name, Object value) {
		conditions.add(new Condition("and", name, "like", value + "%"));
		return this;
	}

	public Criteria andIn(String name, Object[] values) {
		conditions.add(new Condition("and", name, "in ('" + StringUtils.join(values, "','") + "')"));
		return this;
	}

	public Criteria andNotIn(String name, Object[] values) {
		conditions.add(new Condition("and", name, "not in (" + StringUtils.join(values, "','") + ")"));
		return this;
	}

	public Criteria orEqualTo(String name, Object value) {
		conditions.add(new Condition("or", name, "=", value));
		return this;
	}

	public Criteria orNotEqualTo(String name, Object value) {
		conditions.add(new Condition("or", name, "<>", value));
		return this;
	}

	public Criteria orThanOrEqualTo(String name, Object value) {
		conditions.add(new Condition("or", name, ">=", value));
		return this;
	}

	public Criteria orLessOrEqualTo(String name, Object value) {
		conditions.add(new Condition("or", name, "<=", value));
		return this;
	}

	public Criteria orLike(String name, Object value) {
		conditions.add(new Condition("or", name, "like", "%" + value + "%"));
		return this;
	}

	public Criteria orLeftLike(String name, Object value) {
		conditions.add(new Condition("or", name, "like", "%" + value));
		return this;
	}

	public Criteria orIsNull(String name) {
		conditions.add(new Condition("or", name, "is null"));
		return this;
	}

	public Criteria orIsNotNull(String name) {
		conditions.add(new Condition("or", name, "is not null"));
		return this;
	}

	public Criteria orRightLike(String name, Object value) {
		conditions.add(new Condition("or", name, "like", value + "%"));
		return this;
	}

	public Criteria orIn(String name, Object[] values) {
		conditions.add(new Condition("or", name, "in ('" + StringUtils.join(values, "','") + "')"));
		return this;
	}

	public Criteria orNotIn(String name, Object[] values) {
		conditions.add(new Condition("or", name, "not in ('" + StringUtils.join(values, "','") + "')"));
		return this;
	}

	public Criteria orderASC(String name) {
		orders.add(new Order(name, "asc"));
		return this;
	}

	public Criteria orderDESC(String name) {
		orders.add(new Order(name, "desc"));
		return this;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public class Condition {
		private String name;
		private String join;
		private Object value;
		private String operator;
		private String commonOper;

		public Condition(String join, String name, String operator, Object value) {
			this.join = join;
			this.name = name;
			this.operator = operator;
			this.value = value;
		}

		public Condition(String join, String name, String commonOper) {
			this.join = join;
			this.name = name;
			this.commonOper = commonOper;
		}

		public String getCommonOper() {
			return commonOper;
		}

		public void setCommonOper(String commonOper) {
			this.commonOper = commonOper;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getJoin() {
			return join;
		}

		public void setJoin(String join) {
			this.join = join;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}
	}

	public class Order {
		private String name;
		private String order;

		public Order(String name, String order) {
			this.name = name;
			this.order = order;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}
	}
}
