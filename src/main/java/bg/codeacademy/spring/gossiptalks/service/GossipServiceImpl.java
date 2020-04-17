package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GossipServiceImpl implements GossipService
{
  private final GossipRepository gossipRepository;

  @Autowired
  public GossipServiceImpl(GossipRepository gossipRepository)
  {
    this.gossipRepository = gossipRepository;
  }

  @Override
  public List<Gossip> findAllGossipsByUser(User user)
  {
    return gossipRepository.findAllByUser(user);
  }
}
