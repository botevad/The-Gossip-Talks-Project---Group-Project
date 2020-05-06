package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.dto.PageDto;
import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gossips")
public class GossipController
{
  private final GossipServiceImpl         gossipService;
  private final UserServiceImpl           userService;

  @Autowired
  public GossipController(GossipServiceImpl gossipService, UserServiceImpl userService, ApplicationEventPublisher eventPublisher)
  {
    this.gossipService = gossipService;
    this.userService = userService;
  }

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<GossipDto> postGossip(@RequestParam(value = "text") @Valid String text,
                                              Principal principal)
  {
    User currentUser = userService.getUserByUsername(principal.getName()).get();

    Gossip gossip = new Gossip();
    gossip.setText(text);
    gossip.setUser(currentUser);
    gossip.setDatetime(LocalDateTime.now());
    gossipService.saveGossip(gossip);

    GossipDto gossipDto = new GossipDto();
    gossipDto.setId(Integer.toString(gossip.getId(), 32));
    gossipDto.setText(gossip.getText());
    gossipDto.setUsername(gossip.getUser().getUsername());
    gossipDto.setDatetime(gossip.getDatetime());

    return ResponseEntity.ok(gossipDto);
  }

  @GetMapping()
  public ResponseEntity<PageDto> getGossipsOfFriend(

      @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
      Principal principal)

  {
    Pageable pageRequest = PageRequest.of(pageNo, pageSize);
    Page<Gossip> friendsGossips = gossipService.getAllGossipsOfFriends(userService.getUserByUsername(principal.getName()).get(), pageRequest);
    if (pageNo > friendsGossips.getTotalPages()) {
      return ResponseEntity.notFound().build();
    }
    else {
      List<GossipDto> gossipDtos = new ArrayList<>();
      for (Gossip gossip : friendsGossips.getContent()) {
        GossipDto gossipDto = new GossipDto();
        gossipDto.setId(Integer.toString(gossip.getId(), 32));
        gossipDto.setUsername(gossip.getUser().getUsername());
        gossipDto.setDatetime(gossip.getDatetime());
        gossipDto.setText(gossip.getText());
        gossipDtos.add(gossipDto);
      }

      PageDto pageDto = new PageDto();
      pageDto.setNumberOfElemets(pageSize);
      pageDto.setTotalElements(friendsGossips.getTotalElements());
      pageDto.setContent(gossipDtos);
      return ResponseEntity.ok(pageDto);
    }
  }
}
