package com.winning.kbms.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winning.domain.kbms.core.Role;
import com.winning.domain.kbms.core.RoleAuthority;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.service.AuthoritzationService;
import com.winning.kbms.core.service.ManagementService;
import com.winning.kbms.core.service.RoleService;
import com.winning.kbms.core.utils.JackJson;
import com.winning.utils.RandomUIDUtils;

@Controller
@RequestMapping("/role")
public class RoleController extends AbstractManagementController<Role> {

	@Resource(name = "roleService")
	private RoleService roleService;

	@Resource(name = "authoritzationService")
	private AuthoritzationService authoritzationService;

	@Override
	protected ManagementService<Role> getManagementService() {
		return roleService;
	}

	@RequestMapping("/getRoleByRoleId")
	@ResponseBody
	public Role getRoleByRoleId(@RequestParam("roleId") String roleId) {
		return roleService.getById(roleId);
	}

	@RequestMapping("/getRoleByUserName")
	@ResponseBody
	public List<Role> getRoleByUserName(@RequestParam("userName") String userName) {
		return roleService.getRoleByUserName(userName);
	}

	@RequestMapping("/authorizeToRole.do")
	@ResponseBody
	public JResult authorizeToUser(@RequestParam("roleId") String roleId,
			@RequestParam("jsonNodeStr") String jsonNodeStr) {
		authoritzationService.authorizeToRole(roleId, assembleRoleAuthoritys(roleId, jsonNodeStr));
		return new JResult(true, "");
	}

	private List<RoleAuthority> assembleRoleAuthoritys(String roleId, String jsonNodeStr) {
		List<Map<String, String>> nodes = JackJson.fromJsonToObject(jsonNodeStr,
				new TypeReference<List<Map<String, String>>>() {
				});
		List<RoleAuthority> roleAuthoritys = new ArrayList<RoleAuthority>();
		for (Map<String, String> node : nodes) {
			RoleAuthority roleAuthority = new RoleAuthority();
			roleAuthority.setId(RandomUIDUtils.getUUID());
			roleAuthority.setMenuId(node.get("id"));
			// 判断是否是功能节点
			if ("2".equals(node.get("type")) || "3".equals(node.get("type"))) {
				roleAuthority.setAuthChildCode(node.get("authCode"));
				roleAuthority.setAuthType("2".equals(node.get("type")) ? 1 : 2);
				String parentId = node.get("parentId");
				for (Map<String, String> map : nodes) {
					if (StringUtils.equals(parentId, map.get("id"))) {
						roleAuthority.setAuthParentCode(map.get("authCode"));
						break;
					}
				}
			} else {
				if (StringUtils.isNotEmpty(node.get("authCode"))) {
					roleAuthority.setAuthParentCode(node.get("authCode"));
					roleAuthority.setAuthChildCode("read");
				}
			}
			roleAuthority.setRoleId(roleId);
			roleAuthoritys.add(roleAuthority);
		}
		return roleAuthoritys;
	}

}
