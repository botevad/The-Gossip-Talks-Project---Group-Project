package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository        userRepository;
  private final GossipsRepository     gossipsRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, GossipsRepository gossipsRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
  {
    this.userRepository = userRepository;
    this.gossipsRepository = gossipsRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public List<User> getAllUsers(String name)
  {
    Optional<List<User>> allUsers = userRepository.findByNameContaining(name);
    if (!allUsers.isPresent()) {
      return sortedUsers(userRepository.findAll());

    }
    return sortedUsers(allUsers.get());
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
  public List<User> getFriendList(String username)
  {
    User user = getUserByUsername(username).get();


    return user.getFriendList();
  }

  @Override
  public void followUser(String username, User userToAdd)
  {
    User currentUser = getUserByUsername(username).get();
    List<User> friendList = getFriendList(username);
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
  public void saveUserFriendList(String username, List<User> friendList)
  {
    User userToSave = userRepository.findByUsername(username).get();
    userToSave.setFriendList(friendList);
    userRepository.save(userToSave);
  }

  public Integer getGossipsCount(User user)
  {
    return (gossipsRepository.findAllByUser(user).size());
  }

  public List<User> sortedUsers(List<User> users)
  {
    return users.stream()
        .sorted((u1, u2) -> gossipsRepository.findAllByUser(u1).size() - gossipsRepository.findAllByUser(u2).size())
        .collect(Collectors.toList());
  }
}
