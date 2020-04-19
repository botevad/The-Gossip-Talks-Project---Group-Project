package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService
{

  Optional<List<User>> getAllUsers(String name);

  Boolean changePassword(User user, String oldPassword, String password);

  Optional<User> getUserByName(String name);

  Optional<User> getUserByUsername(String username);

  Optional<List<User>> getFollowList(String name);
}

