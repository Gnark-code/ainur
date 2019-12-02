package fr.gnark.sound.port.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyCorsFilter myCorsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(myCorsFilter, CorsFilter.class).cors()
                .and().authorizeRequests()
                .anyRequest().permitAll()
                .and().csrf().disable()
        ;
    }


}
