package bg.codeacademy.spring.gossiptalks.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class UserDto
{
  @NotBlank
  private String  email;
  @NotEmpty
  private String  username;
  private String  name;
  private Boolean following;
}
