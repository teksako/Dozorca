package com.selt.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.sql.DataSource;
@EnableWebMvc
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .usersByUsernameQuery("SELECT u.login, u.password,1 FROM user u WHERE u.login=?")
                .authoritiesByUsernameQuery("SELECT u.login, r.name, 1 " +
                        "FROM user u " +
                        "INNER JOIN user_role ur ON ur.user_id = u.user_id " +
                        "INNER JOIN role r ON r.role_id = ur.role_id " +
                        "WHERE u.login=?")
                .dataSource(dataSource);

    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler(
//                "/webjars/**",
//                "/img/**",
//                "/css/**",
//                "/js/**")
//                .addResourceLocations(
//                        "classpath:/META-INF/resources/webjars/",
//                        "classpath:/static/img/",
//                        "classpath:/static/css/",
//                        "classpath:/static/js/");
//    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/css/**", "/static/css/**", "/js/**");
    }



    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/dba/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/login").permitAll()
                .antMatchers("/index").authenticated()
                .antMatchers("/changePassword").authenticated()
                .antMatchers("/resetPassword").hasAuthority("ADMIN")
                .antMatchers("/showLaptops").authenticated()
                .antMatchers("/addEmployee").authenticated()
                .antMatchers("/addLaptop").hasAuthority("ADMIN")
                .antMatchers("/addWindowsLicense").hasAuthority("ADMIN")
                .antMatchers("/addOfficeLicense").hasAuthority("ADMIN")
                .antMatchers("/addDepartment").hasAuthority("ADMIN")
                .antMatchers("/list-users").authenticated()
                .antMatchers("/list-locations").hasAuthority("ADMIN")
                .antMatchers("/addComputer").hasAuthority("ADMIN")
                .antMatchers("/showUserHardware").hasAuthority("ADMIN")
                .antMatchers("/list-toners").hasAuthority("ADMIN")
                .antMatchers("/addPhone").hasAuthority("ADMIN")
                .antMatchers("/addPhoneNumber").hasAuthority("ADMIN")
                .antMatchers("/list-printers").hasAuthority("ADMIN")
                .antMatchers("/list-employees").hasAuthority("ADMIN")
                .antMatchers("/showUpdateForm").authenticated()
                .antMatchers("/deleteToner").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .defaultSuccessUrl("/index", true);

        httpSecurity.csrf().disable()
                .headers().frameOptions().disable();
//
//        httpSecurity.formLogin()
//                .loginPage("/index")
//                .loginProcessingUrl("/appLogin")
//                .usernameParameter("username")
//                .passwordParameter("pass");
//                //.defaultSuccessUrl("/index", true);
//
//        httpSecurity.csrf().disable()
//                .headers().frameOptions().disable();
//
//        httpSecurity.logout()
//                .logoutSuccessUrl("/login")
//                .logoutUrl("/logout");



    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
