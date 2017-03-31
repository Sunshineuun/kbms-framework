package com.winning.kbms.medical.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharacterEncodingFilter implements Filter {
    private final Log logger = LogFactory.getLog (getClass ());

    private boolean ignore = true;
    private String encoding;
    private FilterConfig filterConfig;
     
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String value = filterConfig.getInitParameter("ignore");
                //ignore就是说,忽略掉页面默认编码,统一采用web.xml中的字符集配置,强制转码
        if (value == null) {
            this.ignore = true;
        } else if (value.equalsIgnoreCase("true")) {
            this.ignore = true;
        } else {
            this.ignore = false;
        }
    }
     
    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        logger.info("开始过滤");
        encoding = getUserEncoding();
        if (ignore && encoding == null) {
            encoding=filterConfig.getInitParameter("encoding");
        }
        if (encoding != null) {
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
            response.setContentType("text/html;charset=" + encoding);
        }
        filterChain.doFilter(request, response);
    }
     
    // 用户项目中定义字符集(配置文件中、在session中获取)
    public String getUserEncoding() {
     
        return null;
    }
     
    @Override
    public void destroy() {
        
    }
     
    }
