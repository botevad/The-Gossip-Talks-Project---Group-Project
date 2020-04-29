package bg.codeacademy.spring.gossiptalks.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationDto
{
  private String email;
  private String username;
  private String name;
  private Boolean following;
  private String password;
  private String passwordConfirmation;

}
