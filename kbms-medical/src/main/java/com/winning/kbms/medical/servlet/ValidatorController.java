package com.winning.kbms.medical.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winning.kbms.core.controller.BaseController;

@Controller
public class ValidatorController extends BaseController
{
	@RequestMapping("validator.do")
	public String validatorCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException
	{
		String incode = (String) request.getParameter("code");
		String rightcode = (String) request.getSession().getAttribute("rCode");
		request.setAttribute("ids", request.getParameter("ids"));
		request.setAttribute("ake001", request.getParameter("ake001"));
		request.setAttribute("ake", request.getParameter("ake"));
		request.setAttribute("xmdm", request.getParameter("xmdm"));
		if (incode != null && rightcode != null)
		{
			if (incode.equals(rightcode))
			{
				request.getSession().getServletContext()
						.setAttribute("validated", true);
				request.getSession().getServletContext()
						.setAttribute("totalCount", 0);

				if (StringUtils.equals("searchDrugByCodeController",
						request.getParameter("method")))
				{
					return this.forward("/ws/drugList/SearchDrugByCode.do");
				}
				else if (StringUtils.equals("SearchDrugByCode",
						request.getParameter("method")))
				{
					return this.forward("/engine/SearchDrugByCode.do");
				}
				else
				{
					return this.forward("/ws/drugList/DrugSearchTerms.do");
				}

			}
			else
			{
				return "errorpage/validator";
			}
		}
		return "errorpage/validator";
	}

}
