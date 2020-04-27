package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.enums.Role;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

  private final UserService userService;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserDetailsServiceImpl(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder)
  {
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
  {
    List<User> users = userService.getAllUsers();
    if (users.isEmpty()) {
      User user = new User();
      user.setUsername("admin");
      user.setName("Administrator");
      user.setEmail("admin@email.com");
      user.setPassword(bCryptPasswordEncoder.encode("1234"));
      user.setRole(Role.ADMIN);
      userService.saveUser(user);
    }

    Optional<User> optionalUser = userService.getUserByUsername(userName);
    if (!optionalUser.isPresent()) {
      throw new UsernameNotFoundException("User not found.");
    }
    User user = optionalUser.get();
    return org.springframework.security.core.userdetails.User.withUsername(userName)
        .password(user.getPassword())
        .roles(user.getRole().toString())
        .build();

  }
}