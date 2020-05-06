package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserService
{

 Set<User> getAllUsers(String name);

 Boolean changePassword(User user, String oldPassword, String password);

 Optional<User> getUserByUsername(String username);

 Set<User> getFriendList(String name);

 void followUser(String username, User userToAdd);

 void saveUser(User user);

 void saveUserFriendList(String username, Set<User> friendList);
}

