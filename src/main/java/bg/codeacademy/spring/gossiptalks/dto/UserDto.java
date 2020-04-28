package bg.codeacademy.spring.gossiptalks.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto
{
  private String  email;
  private String  username;
  private Boolean following;
  private String  name;

}
