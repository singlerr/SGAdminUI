package io.github.singlerr.sgadminui.client;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class GamePlayer {

  private UUID id;
  private String displayName;
  private String role;

}
