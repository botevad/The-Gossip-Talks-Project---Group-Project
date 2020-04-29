package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService
{

  Optional<List<User>> getAllUsers(String name);

  Boolean changePassword(User user, String oldPassword, String password);

  Optional<User> getUserByUsername(String username);

  List<User> getFriendList(String name);

// void followUser(String username, User userToAdd);

  void saveUser(User user);

  void saveUserFriendList(String username, List<User> friendList);
}

