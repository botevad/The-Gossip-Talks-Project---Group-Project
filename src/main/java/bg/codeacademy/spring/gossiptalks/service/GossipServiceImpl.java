package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
  public Page<Gossip> findAllGossipsByUser(User user, Pageable pageable)
  {
    return gossipsRepository.findAllGossipsOfUser(user.getId(), pageable);
  }

  @Override
  public Page<Gossip> getAllGossipsOfFriends(User user, Pageable pageable)
  {
    return gossipsRepository.findAllGossipsOfFriends(user.getId(), pageable);
  }

  @Override
  public void saveGossip(Gossip gossip)
  {
    gossipsRepository.save(gossip);
  }

}
