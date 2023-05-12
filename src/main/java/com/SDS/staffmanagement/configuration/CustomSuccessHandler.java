package com.SDS.staffmanagement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class CustomSuccessHandler  implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        System.out.println("Inside Success Handler");
        if(role.contains("ROLE_HR")){
            response.sendRedirect("/hr/");
        } else if (role.contains("ROLE_MANAGER") ){
            response.sendRedirect("/manager/");
        }
        else{
            response.sendRedirect("/staff/");
        }

    }
}
