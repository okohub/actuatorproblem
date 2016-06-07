package com.okohub;

import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Component;

/**
 * @author obasar
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   @Autowired
   UserDetailsService userDetailsService;

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
      requestCache.setCreateSessionAllowed(false);
      http
          .antMatcher("/api/**").authorizeRequests().antMatchers("/api/hello", "/api/login").permitAll().anyRequest().authenticated()
          .and()
          .exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required!"))
          .and()
          .requestCache().requestCache(requestCache)
          .and()
          .csrf().disable().headers().xssProtection();
   }

   @Autowired
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
      daoAuthenticationProvider.setUserDetailsService(userDetailsService);
      auth.authenticationProvider(daoAuthenticationProvider);
   }

   @Component
   public static class MyUserDetailsService implements UserDetailsService {

      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         if (!Objects.equals(username, "security")) {
            throw new UsernameNotFoundException("Username not found");
         }
         return new User("security", "password", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
      }

   }

}
