package com.winning.kbms.core.resolver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.utils.JackJson;

public class HandlerExceptionResolver extends SimpleMappingExceptionResolver
{
    private final Logger logger = LoggerFactory.getLogger (getClass ());
    private Properties exceptionMappingsByAjax;

    public Properties getExceptionMappingsByAjax ()
    {
        return exceptionMappingsByAjax;
    }

    public void setExceptionMappingsByAjax (Properties exceptionMappingsByAjax)
    {
        this.exceptionMappingsByAjax = exceptionMappingsByAjax;
    }

    @Override
    protected ModelAndView doResolveException (HttpServletRequest request, HttpServletResponse response,
                                               Object handler, Exception ex)
    {

        writeLog (ex);

        String viewName = determineViewName (ex, request);
        if (viewName != null)
        {
            // Apply HTTP status code for error views, if specified.
            // Only apply it if we're processing a top-level request.
            Integer statusCode = determineStatusCode (request, viewName);
            if (statusCode != null)
            {
                applyStatusCodeIfPossible (request, response, statusCode);
            }

            if (isAjaxAccess (request) || ex instanceof ValidationException)
            {
                fillResponse (response, new JResult (false, getAjaxExceptionMessage (ex)));
                return null;
            }
            else
            {
                return getModelAndView (viewName, ex, request);
            }
        }
        else
        {
            return null;
        }
    }

    private void writeLog (Exception ex)
    {
        if (ex instanceof ValidationException || ex instanceof UnknownAccountException
            || ex instanceof UnauthorizedException || ex instanceof IncorrectCredentialsException
            || ex instanceof LockedAccountException || ex instanceof AuthenticationException)
            logger.warn (ex.getMessage ());
        else
            logger.error (ex.getMessage (), ex);
    }

    private String getAjaxExceptionMessage (Exception ex)
    {
        String exceptionMessage = null;

        if (this.getExceptionMappingsByAjax () != null)
            exceptionMessage = this.findMatchingViewName (this.getExceptionMappingsByAjax (), ex);

        if (StringUtils.isEmpty (exceptionMessage))
            exceptionMessage = ex.getMessage ();

        if (StringUtils.isEmpty (exceptionMessage))
            exceptionMessage = "后台出错！";

        return exceptionMessage;
    }

    private void fillResponse (HttpServletResponse response, JResult jResult)
    {
        try
        {
            response.setContentType ("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter ();
            pw.write (JackJson.fromObjectToJson (jResult));
            pw.flush ();
        }
        catch (IOException e)
        {
            logger.error (e.getMessage (), e);
        }
    }

    private boolean isAjaxAccess (HttpServletRequest request)
    {
        String requestType = request.getHeader ("X-Requested-With");
        if (requestType != null && requestType.equals ("XMLHttpRequest")
            || request.getHeader ("accept").indexOf ("application/json") > -1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
