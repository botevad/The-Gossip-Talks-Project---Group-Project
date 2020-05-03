package bg.codeacademy.spring.gossiptalks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
  private String        gossip;
  @ManyToOne
  private User          user;
  @Column(updatable = false)
  private LocalDateTime datetime;
}