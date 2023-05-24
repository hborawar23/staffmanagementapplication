package com.SDS.staffmanagement.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class  MyConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public CustomSuccessHandler customSuccessHandler;
    @Bean
    public UserDetailsService getUserDetailService(){
        return new UserDetailServiceImpl();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        System.out.println("DaoAuthentication++++++++++++++++++++++++++++");
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
        return daoAuthenticationProvider;
    }

    // Configure method
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("Configure Authentication Manager Builder???????????????????");
        auth.authenticationProvider(authenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("Security check----------------------------*****************************");
        http.authorizeRequests()
//                .antMatchers("/hr/**").hasRole("HR")
                .antMatchers("/staff/**").permitAll()
                .antMatchers("/manager/**").permitAll()
                .antMatchers("/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("login")
                .successHandler(customSuccessHandler)
                .and()
                .csrf()
                .disable();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/staff/**").hasRole("STAFF ")
//                .antMatchers("/manager/**").hasRole("MANAGER")
//                .antMatchers("/**").permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//                        if(role.contains("ROLE_STAFF")){
//                            response.sendRedirect("/staff/");
//                        }else if (role.contains("ROLE_MANAGER")) {
//                            response.sendRedirect("/manager/");
//                        }
//                    }
//                })
//                .and().csrf().disable();
//    }
//

}
