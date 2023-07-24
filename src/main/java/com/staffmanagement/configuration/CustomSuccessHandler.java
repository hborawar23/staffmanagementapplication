package com.staffmanagement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/*
    class-level comment
 */
@Configuration
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String > roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());


         if(roles.contains("ROLE_MANAGER"))
        {
            response.sendRedirect("/manager/");
        }
        else if(roles.contains("ROLE_STAFF"))
        {
            response.sendRedirect("/staff/");
        }
        else if (roles.contains("ROLE_HR"))
        {
            response.sendRedirect("/hr/");
        } else{
            response.sendRedirect("/");
         }
    }
}


//    Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//                        if(role.contains("ROLE_STAFF")){
//                            response.sendRedirect("/staff/");
//                        }else if (role.contains("ROLE_MANAGER")) {
//                            response.sendRedirect("/manager/");
//                        }
//                        else {
//                            response.sendRedirect("/hr/");
//                        }

