package bg.codeacademy.spring.gossiptalks.dto;

import bg.codeacademy.spring.gossiptalks.validation.PasswordMatches;
import bg.codeacademy.spring.gossiptalks.validation.ValidEmail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@PasswordMatches
@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationDto
{
  @ValidEmail
  @NotNull
  @NotEmpty
  private String email;
  @NotNull
  @NotEmpty

  private String username;

  private String name;


  @NotBlank
  @NotEmpty
  private String password;
  private String passwordConfirmation;

}
