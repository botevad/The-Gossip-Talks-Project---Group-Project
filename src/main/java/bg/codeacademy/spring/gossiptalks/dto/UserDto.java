package bg.codeacademy.spring.gossiptalks.dto;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDto
{
  @NotNull

  private String email;
  private String username;
  private String name;
  private Boolean following;
  private List<Gossip> gossips;

  public Integer gossipsNumber(){
    return this.gossips.size();
  }
}
