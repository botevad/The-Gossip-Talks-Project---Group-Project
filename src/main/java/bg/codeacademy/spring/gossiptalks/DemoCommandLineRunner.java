package bg.codeacademy.spring.gossiptalks;

import bg.codeacademy.spring.gossiptalks.enums.Role;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DemoCommandLineRunner implements CommandLineRunner
{
  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception
  {
    User user  = new User();
    user.setUsername("user");
    user.setPassword(passwordEncoder.encode("pass"));
    user.setName("Radoslav Nikolov");
    user.setEmail("radoslav@abv.bg");
    List<User> friends = new ArrayList<>();
    user.setFriendList(friends);
    user.grantAuthority(Role.USER);
    userRepository.save(user);
  }
}
