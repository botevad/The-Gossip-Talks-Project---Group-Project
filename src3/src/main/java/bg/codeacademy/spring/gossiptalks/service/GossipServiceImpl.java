package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GossipServiceImpl implements GossipService
{
  private final UserRepository    userRepository;
  private final GossipsRepository gossipsRepository;
  private final UserService       userService;

  @Autowired
  public GossipServiceImpl(UserRepository userRepository, GossipsRepository gossipsRepository, UserService userService)
  {
    this.userRepository = userRepository;
    this.gossipsRepository = gossipsRepository;
    this.userService = userService;
  }

  @Override
  public Page<Gossips> findAllGossipsByUser(User user, Pageable pageable)
  {

    return gossipsRepository.findAllGossipsByUserOrderByDateDesc(user, pageable).get();
  }

  @Override
  public Page<Gossips> getAllGossipsOfFriends(String username, Pageable pageable)
  {
    User currentUser = userRepository.findByUsername(username).get();
    List<User> friendList = userService.getFriendList(username);
    List<Gossips> friendGossips = gossipsRepository
        .findAll()
        .stream()
        .filter(gossips -> friendList.contains(gossips.getUser()))
        .collect(Collectors.toList());
    return new PageImpl(friendGossips, pageable, friendGossips.size());
  }

  @Override
  public void saveGossip(Gossips gossip)
  {
    gossipsRepository.save(gossip);
  }

}
