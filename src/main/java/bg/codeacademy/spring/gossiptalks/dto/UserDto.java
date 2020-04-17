package bg.codeacademy.spring.gossiptalks.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

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
}
