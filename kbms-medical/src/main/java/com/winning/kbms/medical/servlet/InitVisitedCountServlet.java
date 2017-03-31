package com.winning.kbms.medical.servlet;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public class InitVisitedCountServlet extends HttpServlet
{
	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	public static long wait_mills =10000;//10秒
	
	@Override
	public void init()
	{
		ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(
				5);
		final ServletContext application = this.getServletContext();
		application.setAttribute("validated",true);
		application.setAttribute("totalCount",0);
		schedule.scheduleWithFixedDelay(new Runnable()
		{
			@Override
			public void run()
			{
				int count = (Integer)application.getAttribute("visitedCount")==null?0:(Integer)application.getAttribute("visitedCount");
				int totalCount = (Integer)application.getAttribute("totalCount")==null?0:(Integer)application.getAttribute("totalCount");
				if(totalCount>500){
					try
					{
						Thread.sleep(wait_mills);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				if(count>1){
					count --;
				}
				application.setAttribute("visitedCount", count);
			}
		}, 1, 2, TimeUnit.SECONDS);
		
		
		schedule.scheduleWithFixedDelay(new Runnable()
		{
			@Override
			public void run()
			{
				application.setAttribute("totalCount",0);
			}
		}, 0, 2, TimeUnit.HOURS);
	}
}
