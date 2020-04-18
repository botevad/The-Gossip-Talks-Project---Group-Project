package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.dto.UserRegistrationDto;
import bg.codeacademy.spring.gossiptalks.model.User;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService
{
  void addUser(Optional<User> user);
  List<User> findAllUsers(String username);

  boolean changePassword(Principal principal, String oldPassword, String newPassword);

  Optional<User> getUser(String userName);

  void createUser(String username, String password);

  List<User> getUsers();

  void register(UserRegistrationDto userRegistrationDto);
}

