package bg.codeacademy.spring.gossiptalks.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageDto
{
  private Integer         numberOfElemets;
  private Long            totalElements;
  private List<GossipDto> content;
}
