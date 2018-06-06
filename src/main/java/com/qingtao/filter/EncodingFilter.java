package com.qingtao.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * Created by think on 2017/8/21.
 */
public class EncodingFilter implements Filter {
    private String encoder="";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
     encoder= filterConfig.getInitParameter("encoder");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(encoder);
    filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
