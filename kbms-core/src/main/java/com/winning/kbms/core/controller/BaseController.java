package com.winning.kbms.core.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.winning.domain.kbms.core.User;
import com.winning.kbms.core.commons.BooleanEditor;
import com.winning.kbms.core.commons.DoubleEditor;
import com.winning.kbms.core.commons.IntEditor;
import com.winning.utils.web.WebUtils;

/**
 * @author gang.liu
 * @date Jan 16, 2014
 */
public class BaseController
{
    private final Logger logger = LoggerFactory.getLogger (getClass ());

    /**
     * 获取HttpServletRequest
     * 
     * @return
     */
    public HttpServletRequest getRequest ()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes ()).getRequest ();
    }

    /**
     * 获取HttpSession
     * 
     * @return
     */
    protected HttpSession getHttpSession ()
    {
        return getRequest ().getSession ();
    }

    protected HttpSession getSession ()
    {
        // return SecurityUtils.getSubject ().getSession ();
        return getHttpSession ();
    }

    /**
     * 获得Web应用的完整根目录URL
     * 
     * @return
     */
    protected String getServerUrlPath ()
    {
        return WebUtils.getServerUrlPath (getRequest ());
    }

    /**
     * 获得Web应用的物理根目录
     * 
     * @param request
     * @return
     */
    protected String getServerPath ()
    {
        return WebUtils.getRealServerPath (getRequest ());
    }

    protected Map <String, Object> getRequestParameters ()
    {
        return getRequestParameters (getRequest ());
    }

    @SuppressWarnings ("unchecked")
    protected Map <String, Object> getRequestParameters (HttpServletRequest request)
    {
        Iterator <String> keys = request.getParameterMap ().keySet ().iterator ();
        Map <String, Object> result = new HashMap <String, Object> ();
        while (keys.hasNext ())
        {
            String key = keys.next ();
            result.put (key, this.getParamByName (request, key));
        }
        return result;
    }

    protected Object getParamByName (String name)
    {
        return getParamByName (getRequest (), name);
    }

    protected Object getParamByName (HttpServletRequest request, String name)
    {
        Object[] obs = (Object[]) request.getParameterMap ().get (name);
        if (obs != null && obs.length == 1)
        {
            return obs[0];
        }
        else
        {
            return obs;
        }
    }

    /**
     * 获取inpustream内容
     * 
     * @return
     * @throws IOException
     */
    protected String getInputStr () throws IOException
    {
        BufferedReader br = new BufferedReader (new InputStreamReader (getRequest ().getInputStream (), "UTF-8"));
        String line = null;
        StringBuffer sb = new StringBuffer ();
        while ((line = br.readLine ()) != null)
        {
            sb.append (line);
        }

        return sb.toString ();
    }

    /**
     * spring mvc 重定向
     * 
     * @param path
     * @return
     */
    protected String redirect (String path)
    {
        logger.debug ("重定向地址：redirect:/forward" + path);

        return "redirect:" + path;
    }

    /**
     * spring mvc 请求转发
     * 
     * @param path
     * @return
     */
    protected String forward (String path)
    {
        logger.debug ("请求转发地址：forward:/forward" + path);

        return "forward:" + path;
    }

    @InitBinder
    public void initBinder (WebDataBinder binder)
    {
        IntEditor intEditor = new IntEditor ();
        DoubleEditor doubleEditor = new DoubleEditor ();
        BooleanEditor booleanEditor = new BooleanEditor ();
        binder.registerCustomEditor (String.class, new StringTrimmerEditor (false));
        binder.registerCustomEditor (int.class, intEditor);
        binder.registerCustomEditor (Integer.class, intEditor);
        binder.registerCustomEditor (Double.class, doubleEditor);
        binder.registerCustomEditor (double.class, doubleEditor);
        binder.registerCustomEditor (boolean.class, booleanEditor);
        binder.registerCustomEditor (Boolean.class, booleanEditor);
    }

    public User getCurrUser ()
    {
        return (User) SecurityUtils.getSubject ().getPrincipal ();
    }
}
