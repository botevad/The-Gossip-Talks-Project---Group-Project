package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService
{

  Optional<List<User>> getAllUsersByName(String name);

  Boolean changePassword(User user, String oldPassword, String password);

//  Optional<User> getUserByName(String name);

  Optional<User> getUserByUsername(String username);

  List<User> getFollowList(String name);

  void saveUser(User user);
}

