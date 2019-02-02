package com.brothers.premasiri.config;

import com.brothers.premasiri.general.Security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //* @EnableGlobalMethodSecurity(prePostEnabled = true)
//* using this we can manage method access
//*   @PreAuthorize("hasAnyRole('ADMIN')") ..........like
//*


    private final CustomUserDetailsService userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(DataSource dataSource, CustomUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*        http.csrf().disable();
        http.authorizeRequests().antMatchers("/").permitAll();*/

 //for developing easy to give permission all link

        http.
                authorizeRequests()
                //Always users can access without login
                    .antMatchers(
                            "/index",
                            "/favicon.ico",
                            "/img",
                            "/css",
                            "/js",
                            "/font").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/invoiceProcess").permitAll()

                //Need to login for access those are
                    .antMatchers("/employee/**").hasRole("MANAGER")
                    .antMatchers("/user/**").hasRole("MANAGER")
                    .antMatchers("/invoiceProcess/add").hasRole("CHASHIER")
                    .anyRequest()
                    .authenticated()
                .and()
                // Login form
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/mainwindow")
                //Username and password for validation
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                //Logout controlling
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/index")
                    .and()
                    .exceptionHandling()
                //Cross site disable
                .and()
                    .csrf()
                .disable();
   }
}
