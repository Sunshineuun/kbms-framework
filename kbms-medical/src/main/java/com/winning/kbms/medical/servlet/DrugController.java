package com.winning.kbms.medical.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winning.kbms.medical.service.LuceneService;

@Controller
@RequestMapping("/ws/drugList")
public class DrugController {

	@Autowired
	private LuceneService luceneService;
	
	@RequestMapping("DrugSearch.do")
	public String findLinstrFileInfosWithId(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		Map<String, Object> drugMap = luceneService.findLinstrFileInfoWithId(id);
		request.setAttribute("drugMap", drugMap);
		return "DrugSearch";
	}
}
