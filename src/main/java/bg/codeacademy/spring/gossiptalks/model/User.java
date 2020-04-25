package bg.codeacademy.spring.gossiptalks.model;

import bg.codeacademy.spring.gossiptalks.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class User implements UserDetails
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer    id;
  @Column(unique = true, nullable = false)
  @NotBlank
  private String     username;
  @Column(nullable = false)
  private String     password;
  @Column(unique = true, nullable = false)
  private String     email;
  @Column(nullable = false)
  private String     name;
  @ManyToMany
  private List<User> friendList;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  private List<Role> roles;

  @Override
  public List<GrantedAuthority> getAuthorities()
  {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.toString()));
    }
    return authorities;
  }

  public void grantAuthority(Role authority)
  {
    if (roles == null) roles = new ArrayList<>();
    roles.add(authority);
  }

  @Override
  public boolean isAccountNonExpired()
  {
    return true;
  }

  @Override
  public boolean isAccountNonLocked()
  {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @Override
  public boolean isEnabled()
  {
    return true;
  }
}



