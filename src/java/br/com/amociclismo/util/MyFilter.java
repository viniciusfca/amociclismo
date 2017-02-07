/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.amociclismo.util;

import br.com.amociclismo.entity.Usuario;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vinicius
 */
public class MyFilter implements Serializable, Filter {
    
    private static final long serialVersionUID = 1L; 

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;  
        HttpServletResponse res = (HttpServletResponse) response;
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado"); 
        
        if (usuario == null) {
            res.sendRedirect(req.getContextPath());  
        } else {
            chain.doFilter(request, response);   
        }  
    }

    @Override
    public void destroy() {
        
    }
    
}
