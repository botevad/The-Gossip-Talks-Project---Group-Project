package bg.codeacademy.spring.gossiptalks.config;


import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
  @Autowired
  private UserServiceImpl userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void configure(AuthenticationManagerBuilder authenticationMgr) throws Exception
  {
    authenticationMgr.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http.httpBasic()
        .and()
        .authorizeRequests()
        .anyRequest().authenticated();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider()
  {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }
}
