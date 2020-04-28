package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/gossips")
public class GossipController
{
  private final GossipServiceImpl gossipService;
  private final UserServiceImpl   userService;

  @Autowired
  public GossipController(GossipServiceImpl gossipService, UserServiceImpl userService)
  {
    this.gossipService = gossipService;
    this.userService = userService;
  }

  @PostMapping() // TODO Need Advice
  public ResponseEntity<Gossips> postGossip(@RequestBody Gossips gossip, Principal principal)
  {
    User user = userService.getUserByUsername(principal.getName()).get();
    gossip.setUser(user);
    gossipService.addGossip(gossip);
    return ResponseEntity.ok(gossip);
  }

  @GetMapping() //TODO pageNo and pageSize
  public ResponseEntity<List<GossipDto>> getGossipsOfUser(

      @RequestParam(required = false, defaultValue = "0") Integer pageNo,
      @RequestParam(required = false, defaultValue = "20") Integer pageSize,
      @PathVariable String username)

  {
    Optional<User> userCheck = userService.getUserByUsername(username);
    if (!userCheck.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    else {
      User user = userCheck.get();
      List<Gossips> userGossips = gossipService.findAllGossipsByUser(user);
      List<GossipDto> gossipsToShow = new ArrayList<>();
      if (userGossips.isEmpty()) {
        return ResponseEntity.ok(gossipsToShow);
      }
      else {
        for (Gossips g : userGossips
        ) {
          GossipDto gDto = new GossipDto();
          gDto.setUsername(g.getUser().getUsername());
          gDto.setDate(g.getDate());
          gDto.setGossip(g.getGossip());
          gossipsToShow.add(gDto);
        }
      }
      gossipsToShow.sort(Comparator.comparing(GossipDto::getDate));
      return ResponseEntity.ok(gossipsToShow);
    }

  }
}