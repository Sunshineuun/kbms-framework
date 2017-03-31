package com.winning.kbms.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winning.domain.kbms.core.NavMenu;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.service.NavMenuService;
import com.winning.mybatis.support.Criteria;
import com.winning.utils.MapUtils;

@Service("navMenuService")
public class NavMenuServiceImpl
    extends AbstractTreeService<NavMenu>
    implements NavMenuService {
    
	@Override
	public void deleteById(String id) {
		List<NavMenu> list = this.getChildrenById(id);
		if (list == null || list.isEmpty()) {
			this.getDao().deleteById(id, NavMenu.class);
		}else{
			throw new ValidationException("存在子节点，无法删除！");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<NavMenu> getAllChildrenById(String id) {
		List<NavMenu> list;
		if ("0".equals(id)) {
			list = getDao().getAll(NavMenu.class);
		} else {
			list = getAllChildrenByParentId(id);
		}
		return assembleTreeByParentId(id, list);
	}

	private List<NavMenu> getAllChildrenByParentId(String parentId) {
		List<NavMenu> navMenus = this.queryByCriteria(Criteria.create()
				.andEqualTo("parentId", parentId)
				.andEqualTo("viewFlag", true));
		if (navMenus == null || navMenus.isEmpty())
			return new ArrayList<NavMenu>(1);
		
		List<NavMenu> list = new ArrayList<NavMenu>(navMenus);
		for (NavMenu navMenu : navMenus) {
			list.addAll(getAllChildrenByParentId(navMenu.getId()));
		}
		return list;
	}

	/**
	 * 获取导航菜单
	 * 
	 * @param id
	 * @return
	 */
	public List<NavMenu> getAllNavMenuChildrenById(String id) {
		List<NavMenu> list = this.getDao().queryByCriteria(
				Criteria.create().andEqualTo("id", id).andEqualTo("type", "1"), NavMenu.class);
		return assembleTreeByParentId(id, list);
	}

	/**
	 * 获取含有用户权限的导航菜单包括功能点
	 * 
	 * @param id
	 * @return
	 */
	public List<NavMenu> getNavMeunFunWithUserAuth(String id, String userName) {
		List<NavMenu> list = getDao().query("getNavMeunFunWithUserAuth",
				MapUtils.newMap("id", id, "userName", userName));
		for (NavMenu navMenu : list) {
			if (navMenu.getType() == 1) {
				if (navMenu.isLeaf())
					navMenu.setIconCls("/kbms/resources/js/extjs4/resources/ext-theme-gray/images/tree/leaf.gif");
				else
					navMenu.setIconCls(null);

				navMenu.setLeaf(false);
			} else {
				navMenu.setIconCls("/kbms/resources/images/icons/cog.png");
				navMenu.setLeaf(true);
			}
		}
		return assembleTreeByParentId(id, list);
	}

	public List<NavMenu> getNavMenuChildrenByIdWithAuth(String id, String userName) {
		return getDao().query("getNavMenuChildrenByIdWithAuth", MapUtils.newMap("id", id, "userName", userName));
	}

	public List<NavMenu> getAllNavMenuChildrenByIdWithAuth(String id, String userName) {
		List<NavMenu> list = getDao().query("getAllNavMenuChildrenByIdWithAuth",
				MapUtils.newMap("id", id, "userName", userName));
		return assembleTreeByParentId(id, list);
	}

	@Override
	public List<NavMenu> getNavMeunFunWithRoleAuth(String id, String roleId) {
		List<NavMenu> list = getDao().query("getNavMeunFunWithRoleAuth", MapUtils.newMap("id", id, "roleId", roleId));
		for (NavMenu navMenu : list) {
			if (navMenu.getType() == 1) {
				if (navMenu.isLeaf())
					navMenu.setIconCls("/kbms/resources/js/extjs4/resources/ext-theme-gray/images/tree/leaf.gif");
				else
					navMenu.setIconCls(null);

				navMenu.setLeaf(false);
			} else {
				navMenu.setIconCls("/kbms/resources/images/icons/cog.png");
				navMenu.setLeaf(true);
			}
		}
		return assembleTreeByParentId(id, list);
	}

	@Override
	public void cutNavMenu(String id, String parentId) {
		getDao().update("cutNavMenu", MapUtils.newMap("id", id, "parentId", parentId));
	}

	@Override
	public List<NavMenu> getRuleRootNavMenu(String id) {
		List<NavMenu> list = new ArrayList<NavMenu>();
		/**
		 * 规则配置目录ID，目前先硬编码
		 */
		String[] ruleConfigId = {"adafd81f-9743-4510-bf8d-6ec40ffaf4f1","5BC76D34A2004B82B83C71E29BBB0500"};
		for(String s : ruleConfigId){
			NavMenu ruleConfigTree = this.getById(s);
			ruleConfigTree.setExpanded(true);
			list.add(ruleConfigTree);
			list.addAll(getRuleAllChildrenByParentId(s));
		}
		for(NavMenu o : list){
			if(o.getName().equals("药品规则配置")
					|| o.getName().equals("药品规则配置-南京版")){
				o.setLeaf(false);
				o.setIconCls("/kbms/resources/js/extjs4/resources/ext-theme-gray/images/tree/leaf.gif");
			}
		}
		return assembleTreeByParentId(id, list);
	}
	
	/**
	 * 
	 * @author:qiushengming
	 * @param parentId
	 * @return
	 * @return:List<NavMenu>
	 * @date:2017-2-17 下午1:23:31
	 * <p>为【专家审核】需求提供查询</p>
	 */
	private List<NavMenu> getRuleAllChildrenByParentId(String parentId) {
		List<NavMenu> navMenus = this.queryByCriteria(Criteria.create()
				.andEqualTo("parentId", parentId)
				.andEqualTo("viewFlag", true)
				.andEqualTo("type", 1));
		if (navMenus == null || navMenus.isEmpty())
			return new ArrayList<NavMenu>(1);
		
		List<NavMenu> list = new ArrayList<NavMenu>(navMenus);
		for (NavMenu navMenu : navMenus) {
			list.addAll(getRuleAllChildrenByParentId(navMenu.getId()));
		}
		return list;
	}
}
