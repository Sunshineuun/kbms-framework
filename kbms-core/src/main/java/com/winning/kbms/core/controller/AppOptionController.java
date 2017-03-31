package com.winning.kbms.core.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winning.domain.kbms.core.AppOption;
import com.winning.kbms.core.commons.Constants;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.service.AppOptionService;
import com.winning.kbms.core.service.ManagementService;

@Controller
@RequestMapping("/appOption")
public class AppOptionController extends AbstractManagementController<AppOption> {
	@Resource(name = "kbmsAppOptionService")
	private AppOptionService appOptionService;

	@RequestMapping("/execute.do")
	@ResponseBody
	public JResult execute(AppOption obj, @RequestParam("oper") String oper) throws Exception {
		try {
			if (oper.equals(Constants.OPER_ADD)) {
				addValidation(obj);
				if (StringUtils.isEmpty(obj.getId()))
					obj.setId(generateId());
				if (obj.getUpdateTime() == null)
					obj.setUpdateTime(new Date());
				if (obj.getUpdateBy() == null)
					obj.setUpdateBy(this.getCurrUser().getUserName());

				this.add(obj);
				return new JResult(true, "新增成功！");
			} else if (oper.equals(Constants.OPER_EDIT)) {
				editValidation(obj);
				if (obj.getUpdateTime() == null)
					obj.setUpdateTime(new Date());
				if (obj.getUpdateBy() == null)
					obj.setUpdateBy(this.getCurrUser().getUserName());

				this.update(obj);
				return new JResult(true, "修改成功！");
			} else if (oper.equals(Constants.OPER_DEL)) {
				delValidation(obj);
				appOptionService.deleteByLbdm(obj.getLbdm());
				return new JResult(true, "删除成功！");
			} else {
				throw new ValidationException("\"oper\"的值必须为：add或edit或del或batchDel");
			}
		} catch (ValidationException e) {
			return new JResult(false, e.getMessage());
		}
	}

	@Override
	protected ManagementService<AppOption> getManagementService() {
		return appOptionService;
	}

}
