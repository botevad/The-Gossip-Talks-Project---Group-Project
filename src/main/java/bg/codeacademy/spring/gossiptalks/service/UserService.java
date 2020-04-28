package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService
{

 List<User> getAllUsersByName(String name);

 Boolean changePassword(User user, String oldPassword, String password);

 Optional<User> getUserByUsername(String username);

 List<User> getFollowList(String name);

 void saveUser(User user);

 List<User> getAllUsers();
}

