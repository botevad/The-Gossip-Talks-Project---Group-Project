package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository        userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
  {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public Optional<List<User>> getAllUsers(String name)
  {
    Optional<List<User>> allUsers = userRepository.findByNameContaining(name);
    if (!allUsers.isPresent()) {
      return Optional.of(userRepository.findAll());
    }
    return Optional.of(allUsers.get()
        .stream()
        .sorted(Comparator.comparing(User::countFriends))
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
  public Optional<User> getUserByUsername(String username)
  {

    return Optional.ofNullable(userRepository.findByUsername(username).orElse(null));
  }

  @Override
  public List<User> getFriendList(String username)
  {
    User user = getUserByUsername(username).get();
//    if (user.getFriendList() == null) {
//      user.setFriendList(new ArrayList<>());
//    }
    return user.getFriendList();
  }

//  @Override
//  public void followUser(String username, User userToAdd)
//  {
//    List<User> friendList = getFriendList(username);
//    friendList.add(userToAdd);
//    getUserByUsername(username).get().setFriendList(friendList);
//
//  }

  @Override
  public void saveUser(User user)
  {
    this.userRepository.save(user);
  }

  @Override
  public void saveUserFriendList(String username, List<User> friendList)
  {
    User user = userRepository.findByUsername(username).get();
    user.setFriendList(friendList);
    userRepository.save(user);
  }
}
