package bg.codeacademy.spring.gossiptalks.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

  @Override
  protected void configure(HttpSecurity http) throws Exception

  {
    http
        .authorizeRequests()
        .antMatchers("/", "/api/v1/users/**", "/api/v1/gossip/**")
        .permitAll()
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .and()
        .formLogin().loginPage("/api/v1/users/login").permitAll()
        .usernameParameter("username")
        .passwordParameter("password")
        .and()
        .logout()
        .logoutSuccessUrl("/login?logout")
        .permitAll();

    http.csrf().disable();
    http.headers().frameOptions().disable();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception
  {
    authenticationMgr.inMemoryAuthentication().withUser("admin").password("admin").authorities("ROLE_USER");

  }

  @Bean
  BCryptPasswordEncoder bCryptPasswordEncoder()
  {
    return new BCryptPasswordEncoder();
  }
}
