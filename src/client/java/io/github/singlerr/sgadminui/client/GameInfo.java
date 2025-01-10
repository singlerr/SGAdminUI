package io.github.singlerr.sgadminui.client;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public final class GameInfo {

  private final Game info;
  private final PlayerList playerList;

  @Data
  @NoArgsConstructor
  public static class Builder {

    private PlayerList playerList;
    private Game game;

    public boolean setPlayerList(PlayerList list) {
      this.playerList = list;
      return game != null && playerList != null;
    }

    public boolean setGame(Game game) {
      this.game = game;
      return game != null && playerList != null;
    }

    public GameInfo build() {

      GameInfo i = new GameInfo(game, playerList);
      game = null;
      playerList = null;
      return i;
    }
  }

  @Data
  @AllArgsConstructor
  public static class PlayerList {
    private String id;
    private List<GamePlayer> playerList;
  }

  @Data
  @AllArgsConstructor
  public static class Game {
    private String currentGameId;
    private GameHistory currentGame;
    private List<GameHistory> games;
  }
}
