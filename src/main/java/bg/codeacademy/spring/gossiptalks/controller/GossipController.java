package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


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

  @PostMapping(produces = {"application/json"},
      consumes = {"multipart/form-data"})
  public ResponseEntity<GossipDto> postGossip(@RequestParam(value = "text") @Valid String gossip,
                                            Principal principal)
  {
    Gossips newGossip = new Gossips();
    newGossip.setGossip(gossip);
    User user = userService.getUserByUsername(principal.getName()).get();
    newGossip.setUser(user);
    gossipService.addGossip(newGossip);
    GossipDto gDto = new GossipDto();
    gDto.setId(Integer.toString(newGossip.getId(), 32));
    gDto.setUsername(newGossip.getUser().getUsername());
    gDto.setDate(newGossip.getDate());
    gDto.setGossip(newGossip.getGossip());
    return ResponseEntity.ok(gDto);
  }

  @GetMapping()
  public ResponseEntity<List<GossipDto>> getGossipsOfFriends(Principal principal)
  {
    User user = userService.getUserByUsername(principal.getName()).get();
    List<User> friends = user.getFriendList();
    List<GossipDto> gossipsToShow = new ArrayList<>();
    if (!friends.isEmpty()) {
      for (User friend : friends) {
        List<Gossips> userGossips = gossipService.findAllGossipsByUser(friend);
        if (!userGossips.isEmpty()) {
          for (Gossips g : userGossips
          ) {
            GossipDto gDto = new GossipDto();
            gDto.setId(Integer.toString(g.getId(), 32));
            gDto.setUsername(g.getUser().getUsername());
            gDto.setDate(g.getDate());
            gDto.setGossip(g.getGossip());
            gossipsToShow.add(gDto);
          }
        }
      }
    }
    gossipsToShow.sort(Comparator.comparing(GossipDto::getDate));
    return ResponseEntity.ok(gossipsToShow);
  }
}