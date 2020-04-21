package bg.codeacademy.spring.gossiptalks.dto;

import bg.codeacademy.spring.gossiptalks.validation.NotHTML;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GossipDto
{
  @NotNull(message = "The gossip cannot be NULL!")
  @NotHTML
  private String        gossip;
  private LocalDateTime date;
  @NotNull()
  private String        username;
}
