package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipDto;
import bg.codeacademy.spring.gossiptalks.dto.PageDto;
import bg.codeacademy.spring.gossiptalks.model.Gossips;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipsRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import bg.codeacademy.spring.gossiptalks.service.GossipServiceImpl;
import bg.codeacademy.spring.gossiptalks.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  private final ApplicationEventPublisher eventPublisher;
  private final GossipsRepository         gossipsRepository;
  private final UserRepository            userRepository;

  @Autowired
  public GossipController(GossipServiceImpl gossipService, UserServiceImpl userService, ApplicationEventPublisher eventPublisher, GossipsRepository gossipsRepository, UserRepository userRepository)
  {
    this.gossipService = gossipService;
    this.userService = userService;
    this.eventPublisher = eventPublisher;
    this.gossipsRepository = gossipsRepository;
    this.userRepository = userRepository;
  }

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<GossipDto> postGossip(@RequestParam String text,
                                              Principal principal)
  {
    User currentUser = userService.getUserByUsername(principal.getName());

    Gossips gossip = new Gossips();
    gossip.setGossip(text);
    gossip.setUser(currentUser);
    gossip.setDate(LocalDateTime.now());
    gossipService.saveGossip(gossip);

    GossipDto gossipDto = new GossipDto();
    gossipDto.setId(Integer.toString(gossip.getId(), 32));
    gossipDto.setText(gossip.getGossip());
    gossipDto.setUsername(gossip.getUser().getUsername());
    gossipDto.setDatetime(gossip.getDate());

    return ResponseEntity.ok().header("responseHeader", "Successful operation").body(gossipDto);
  }

  @GetMapping()
  public ResponseEntity<PageDto> getGossipsOfUser(

      @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
      Principal principal)

  {

    User currentUser = userService.getUserByUsername(principal.getName());
    List<Gossips> allGossips = gossipService.getAllGossipsOfFriends(principal.getName());
    //   Page<Gossips> friendsGossips = new PageImpl(allGossips, pageRequest, allGossips.size() );
    List<GossipDto> gossipDtos = new ArrayList<>();
    for (Gossips gossips : allGossips) {
      GossipDto gDto = new GossipDto();
      gDto.setId(Integer.toString(gossips.getId(), 32));
      gDto.setUsername(gossips.getUser().getUsername());
      gDto.setDatetime(gossips.getDate());
      gDto.setText(gossips.getGossip());
      gossipDtos.add(gDto);
    }
    Pageable pageRequest = PageRequest.of(pageNo, pageSize);
    Page<User> page = new PageImpl(gossipDtos, pageRequest, gossipDtos.size());
    PageDto pageDto = new PageDto();
    pageDto.setNumberOfElemets(page.getSize());
    pageDto.setTotalElements((long) gossipDtos.size());
    pageDto.setContent(page.getContent());
    return ResponseEntity.ok(pageDto);
  }

}
