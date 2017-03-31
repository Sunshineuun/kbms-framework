package com.winning.kbms.medical.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.winning.kbms.medical.service.DrugService;
import com.winning.kbms.medical.utils.Constants;

public class DrugServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private DrugService drugService;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		drugService = (DrugService)Constants.SPRING_CONTAINER.getBean("drugService");
		String idString=request.getParameter("id");
		Integer id;
		try{
			id=Integer.parseInt(idString);
		}catch (Exception e) {
			id=1;
		}
		Map<String, Object> drugMap=drugService.findLinstrFileInfosWithId(id);
		request.setAttribute("drugMap", drugMap);
		request.getRequestDispatcher("searchPages/DrugSearch.jsp").forward(request, response);
		out.flush();
		out.close();
	}
}
