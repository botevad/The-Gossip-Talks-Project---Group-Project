package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository        userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final GossipsRepository     gossipsRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, GossipsRepository gossipsRepository)
  {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.gossipsRepository = gossipsRepository;
  }

  @Override
  public Optional<List<User>> getAllUsers(String name)
  {
    Optional<List<User>> allUsers = userRepository.findByNameContaining(name);
    if (!allUsers.isPresent()) {
      return Optional.of(new ArrayList<>());
    }
    return Optional.of(allUsers.get()
        .stream()
        .sorted(Comparator.comparing(user -> gossipsRepository.findAllGossipsByUserOrderByDateDesc(user).get().size()))
        .collect(Collectors.toList()));
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
  public User getUserByUsername(String username)
  {

    return (userRepository.findByUsername(username).orElse(null));
  }

  @Override
  public List<User> getFriendList(String username)
  {
    return getUserByUsername(username).getFriendList();
  }

  @Override
  public void followUser(String username, User userToAdd)
  {
    User currentUser = getUserByUsername(username);
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
}
