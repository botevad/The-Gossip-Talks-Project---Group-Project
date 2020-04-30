package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GossipServiceImpl implements GossipService
{
  private final GossipsRepository gossipsRepository;

  @Autowired
  public GossipServiceImpl(GossipsRepository gossipsRepository)
  {
    this.gossipsRepository = gossipsRepository;
  }

  @Override
  public List<Gossips> findAllGossipsByUser(User user)
  {
    return gossipsRepository.findAllGossipsByUser(user).get();
  }


  @Override
  public void addGossip(Gossips gossip)
  {
    gossipsRepository.save(gossip);
  }

}
