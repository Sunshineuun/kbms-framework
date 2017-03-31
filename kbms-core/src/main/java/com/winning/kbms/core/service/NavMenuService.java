package com.winning.kbms.core.service;

import java.util.List;

import com.winning.domain.kbms.core.NavMenu;

public interface NavMenuService
{
    public List <NavMenu> getNavMeunFunWithUserAuth (String id, String userName);

    public List <NavMenu> getNavMeunFunWithRoleAuth (String id, String roleId);

    public List <NavMenu> getNavMenuChildrenByIdWithAuth (String id, String userName);

    public List <NavMenu> getAllNavMenuChildrenByIdWithAuth (String id, String userName);
    
    public void cutNavMenu (String id, String parentId);

	/**
	 * @author:qiushengming
	 * @param ids
	 * @return
	 * @return:List<NavMenu>
	 * @date:2017-2-16 下午3:42:42
	 * <p>将硬编码写在代码中的两个ID，及其叶子节点查找出来，并且第一个节点是打开的
	 * </br>这两个根节点分别为【规则配置】和【规则配置2012版】
	 * </br>处理方式：将该两节点下所有的子节点取出来。在进行排序。</p>
	 */
	public List<NavMenu> getRuleRootNavMenu(String ids);

}