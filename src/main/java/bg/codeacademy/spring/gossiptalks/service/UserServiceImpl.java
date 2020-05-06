package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository        userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, GossipsRepository gossipsRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
  {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public Set<User> getAllUsers(String username)
  {
    return userRepository.findUser(username);
  }

  @Override
  public Boolean changePassword(User user, String oldPassword, String password)
  {
    if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
      user.setPassword(bCryptPasswordEncoder.encode(password));
      userRepository.save(user);
      return true;
    }
    return false;
  }

  @Override
  public Optional<User> getUserByUsername(String username)
  {
    return Optional.ofNullable(userRepository.findByUsername(username).orElse(null));
  }

  @Override
  public Set<User> getFriendList(String username)
  {
    Set<User> userFriendList = getUserByUsername(username).get().getFriendList();
    if (!userFriendList.contains(getUserByUsername(username))) {
      userFriendList.add(getUserByUsername(username).get());
    }
    return userFriendList;
  }

  @Override
  public void followUser(String username, User userToAdd)
  {
    User currentUser = getUserByUsername(username).get();
    Set<User> friendList = getFriendList(username);
    friendList.add(userToAdd);
    currentUser.setFriendList(friendList);
    userRepository.save(currentUser);
  }

  @Override
  public void saveUser(User user)
  {
    this.userRepository.save(user);
  }

  @Override
  public void saveUserFriendList(String username, Set<User> friendList)
  {
    User userToSave = userRepository.findByUsername(username).get();
    userToSave.setFriendList(friendList);
    userRepository.save(userToSave);
  }

}
