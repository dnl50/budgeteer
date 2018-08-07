package org.wickedsource.budgeteer.service.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.components.security.NeedsLogin;

/**
 * A configuration to set up spring boot security. Only used for authorization. Authentication
 * is performed via the {@link NeedsLogin} annotation and the {@link UserService}.
 *
 * @see org.wickedsource.budgeteer.web.BudgeteerSession#login(User)
 * @see UserService#login(String, String)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .headers()
                    .frameOptions()
                        .sameOrigin() // allow iframes on the same domain (required for editing contracts etc.)
                .and()
                .csrf()
                    .csrfTokenRepository(new CookieCsrfTokenRepository())
                    .ignoringAntMatchers("/register*", "/login*", "/selectProject*")
                .and()
                .servletApi()
                    .disable() // disables the SecurityContextHolderAwareRequestFilter that wraps requests
                .antMatcher("/**")
                    .anonymous(); // @NeedsLogin annotation deals with allowing authenticated access only
        // @formatter:on
    }

    @Bean(name = "filterMultipartResolver")
    public CommonsMultipartResolver filterMultipartResolver(){
        return new CommonsMultipartResolver();
    }

}
