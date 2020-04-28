package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.enums.Role;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
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

  private final UserRepository        userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
  {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
  {
    List<User> users = userRepository.findAll();
    if (users.isEmpty()) {
      User user = new User();
      user.setUsername("admin");
      user.setName("Administrator");
      user.setEmail("admin@email.com");
      user.setPassword(bCryptPasswordEncoder.encode("1234"));
      user.setRole(Role.ADMIN);
      userRepository.save(user);
    }

    Optional<User> optionalUser = userRepository.findByUsername(userName);
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