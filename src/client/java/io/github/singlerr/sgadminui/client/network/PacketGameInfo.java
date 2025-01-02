package io.github.singlerr.sgadminui.client.network;

import io.github.singlerr.sgadminui.client.GameHistory;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@NoArgsConstructor
@AllArgsConstructor
@Data
public final class PacketGameInfo implements FabricPacket {

  public static final PacketType<PacketGameInfo> TYPE = PacketType.create(new ResourceLocation("sgadmin", "game_info"), PacketGameInfo::new);

  private String currentGameId;
  private List<GameHistory> games;

  public PacketGameInfo(FriendlyByteBuf buf){
    readPayload(buf);
  }

  @Override
  public void write(FriendlyByteBuf buffer) {
    buffer.writeUtf(currentGameId);
    buffer.writeInt(games.size());
    for (GameHistory game : games) {
      writeGame(game, buffer);
    }
  }

  private void writeGame(GameHistory game, FriendlyByteBuf buffer){
    buffer.writeUtf(game.getId());
    buffer.writeInt(game.getPlayerCount());
    buffer.writeInt(game.getSurvivorsCount());
  }

  private GameHistory readGame(FriendlyByteBuf buffer){
    return new GameHistory(buffer.readUtf(), buffer.readInt(), buffer.readInt());
  }

  public void readPayload(FriendlyByteBuf buffer) {
    currentGameId = buffer.readUtf();
    int size = buffer.readInt();
    List<GameHistory> games = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      games.add(readGame(buffer));
    }
    this.games = games;
  }

  @Override
  public PacketType<?> getType() {
    return TYPE;
  }
}
