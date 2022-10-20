package com.neogility.suitetalk.configuration;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.HypermediaWebClientConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.web.reactive.function.client.WebClient;

import com.neogility.suitetalk.oauth2.netsuite.NetsuiteAuthorizationRequestResolver;

import io.netty.handler.logging.LogLevel;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http, NetsuiteAuthorizationRequestResolver resolver) throws Exception {
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		http
		.csrf(c -> c
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.ignoringAntMatchers("/login/oauth2/code/netsuite")
        )
		.authorizeRequests(a -> a
			.antMatchers("/netsuite/auth/oauth2/v1/userinfo","/actuator/**","/error").permitAll()
			.anyRequest().authenticated()
		)
		.logout(l -> l
			.logoutSuccessUrl("/").permitAll()
		)
		.exceptionHandling(e -> e
			//.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new NegatedRequestMatcher(new AntPathRequestMatcher("/login/oauth2/code/netsuite")))
		)
        .oauth2Login()
        .authorizationEndpoint()
        .authorizationRequestResolver(resolver);

        return http.build();
    }

	
	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
        		.debug(true)
        		.ignoring().antMatchers(
        				// the standard favicon URI
        				"/favicon.ico",

        				// the robots exclusion standard
        				"/robots.txt",
           				// icons and images
        				"/icons/**",
        				"/images/**",
        				"/img/**",
        				"/css/**",
        				"/js/**",
        				
        				"/actuator/**");
    }
	
    /*
     * Configure the WebClient to get the token from the oauth login
     */
    @Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrationRepository, 
      OAuth2AuthorizedClientRepository authorizedClientRepository
      ,HypermediaWebClientConfigurer configurer
      ) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = 
          new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, 
          authorizedClientRepository);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        
        HttpClient httpClient = HttpClient
        		  .create()
        		  .wiretap("reactor.netty.http.client.HttpClient", 
        		    LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        
        return configurer.registerHypermediaTypes(WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))).apply(oauth2.oauth2Configuration()).build();
    }
	

}