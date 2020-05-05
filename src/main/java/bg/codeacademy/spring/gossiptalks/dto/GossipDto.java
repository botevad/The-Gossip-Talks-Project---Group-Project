package bg.codeacademy.spring.gossiptalks.dto;

import bg.codeacademy.spring.gossiptalks.validation.NotHTML;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GossipDto
{

  private String id;

  @NotNull()
  private String username;

  private LocalDateTime datetime;

  @NotBlank(message = "The gossip cannot be blank!")
  @NotHTML
  private String text;


}
