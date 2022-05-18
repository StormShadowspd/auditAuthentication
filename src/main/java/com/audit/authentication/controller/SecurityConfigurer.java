package com.audit.authentication.controller;

import com.audit.authentication.service.AuthService;
import com.audit.authentication.service.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
This class is used for the security configuration. It extends the
*          class WebSecurityConfigurerAdapter It will take care of the
*          authentication and authorization based on the user credentials.
*
*/

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private AuthService authService;

	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	/**
	 * This methods used to override the credentials which spring automatically
	 * generates By using {@link AuthenticationManagerBuilder} object we are
	 * overriding the security credentials with our own given credentials It will
	 * call the userDetailsService method on the
	 * {@link AuthenticationManagerBuilder} class object and this method is present
	 * in class {@link AuthenticationManagerBuilder}
	 */
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		super.configure(auth);
		auth.userDetailsService(authService);
	
	}
	
	/**
	 * This method is used to inject a {@link AuthenticationManager} type bean We
	 * are annotating this method with @Bean. @Bean annotation tells that a method
	 * produces a bean to be managed by the Spring container.
	 */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated()
		.and().exceptionHandling().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.headers().cacheControl().disable();
		http.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs","/configuration/ui",
				"/swagger-resources/**","/configuration/security","/swagger-ui.html","/webjars/**","/auth/swagger");
	}


}
