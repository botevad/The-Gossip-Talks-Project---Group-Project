package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/gossips")
public class GossipController
{
  private final GossipServiceImpl gossipService;

  @Autowired
  public GossipController(GossipServiceImpl gossipService)
  {
    this.gossipService = gossipService;
  }

  @GetMapping("/users/{username}/gossips")
  public ResponseEntity<List<Gossip>> showUserGossips(User user)
  {
    return null;
  }

}
