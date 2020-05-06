package bg.codeacademy.spring.gossiptalks.model;

import bg.codeacademy.spring.gossiptalks.validation.NotHTML;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class Gossip
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer       id;
  @Column(updatable = false)
  @NotHTML
  @NotBlank
  private String        text;
  @ManyToOne
  private User          user;
  @Column(updatable = false)
  private LocalDateTime datetime;
}
