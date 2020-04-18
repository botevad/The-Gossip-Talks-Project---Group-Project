package bg.codeacademy.spring.gossiptalks.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http
        .authorizeRequests()
        .antMatchers("/api/v1/users/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .csrf().disable();

//    http
//        .authorizeRequests()
//        .antMatchers(HttpMethod.GET, "/browser/index.html#/").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
//        .antMatchers(HttpMethod.GET, "/api/v1/books/**").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
//        .antMatchers(HttpMethod.POST, "/api/v1/books/**").hasRole(Role.ADMIN.toString())
//        .antMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasRole(Role.ADMIN.toString())
//
//        .antMatchers(HttpMethod.GET, "/api/v1/ratings/**").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
//        .antMatchers(HttpMethod.POST, "/api/v1/ratings/**").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
//
//        .antMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole(Role.ADMIN.toString())
//        .antMatchers(HttpMethod.POST, "/api/v1/users/{^[\\d]$}/password").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
//        .antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole(Role.ADMIN.toString())
//        .antMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole(Role.ADMIN.toString())
//
//        .and()
//        .httpBasic();
//
//    http.csrf().disable();
//    http.headers().frameOptions().disable();
  }

  @Bean
  PasswordEncoder passwordEncoder()
  {
    return new BCryptPasswordEncoder();
  }


}
