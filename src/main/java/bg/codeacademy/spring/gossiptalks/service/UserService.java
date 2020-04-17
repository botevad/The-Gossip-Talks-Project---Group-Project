package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService
{
  void addUser(Optional<User> user);
  List<User> findAllUsers(String username);
}

