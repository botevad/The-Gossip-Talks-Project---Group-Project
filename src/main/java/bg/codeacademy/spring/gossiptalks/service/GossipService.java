package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;

import java.util.List;
import java.util.Optional;

public interface GossipService
{
  Optional<List<Gossips>> findAllGossipsByUser(User user);

  void addGossip(Gossips gossip);
}
