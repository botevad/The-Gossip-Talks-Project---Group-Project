package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;

import java.util.List;

public interface GossipService
{
  List<Gossip> findAllGossipsByUser(User user);
}
