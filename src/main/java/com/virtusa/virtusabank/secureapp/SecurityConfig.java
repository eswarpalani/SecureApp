package com.virtusa.virtusabank.secureapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer.ContextSourceBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Security Configuration - LDAP and HTTP Authorizations.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		ContextSourceBuilder contextSourceBuilder = auth.ldapAuthentication().userSearchBase("ou=people")
				.userSearchFilter("(uid={0})").groupSearchBase("ou=groups").groupSearchFilter("(member={0})")
				.contextSource().root("dc=virtusa,dc=com").ldif("classpath:users.ldif");

		contextSourceBuilder.and().passwordCompare().passwordEncoder(new LdapShaPasswordEncoder())
				.passwordAttribute("userPassword");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/anonymous*").anonymous().antMatchers("/login", "/")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login")
				.loginProcessingUrl("/login").failureUrl("/login.html?error=true").and().logout()
				.deleteCookies("JSESSIONID").and().rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400).and()
				.sessionManagement().sessionFixation().migrateSession()
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
				.maximumSessions(2).expiredUrl("/sessionExpired");
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

}
