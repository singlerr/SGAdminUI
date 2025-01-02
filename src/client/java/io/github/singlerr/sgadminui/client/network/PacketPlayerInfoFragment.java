package io.github.singlerr.sgadminui.client.network;

import io.github.singlerr.sgadminui.client.GamePlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Data
@AllArgsConstructor
public final class PacketPlayerInfoFragment implements FabricPacket {

  public static final PacketType<PacketPlayerInfoFragment> TYPE = PacketType.create(new ResourceLocation("sgadmin","player_info"), PacketPlayerInfoFragment::new);

  private String id;
  private List<GamePlayer> playerList;

  public PacketPlayerInfoFragment(FriendlyByteBuf buf){
    readPayload(buf);
  }

  @Override
  public void write(FriendlyByteBuf buffer) {
    buffer.writeUtf(id);
    buffer.writeInt(playerList.size());
    for (GamePlayer p : playerList) {
      writeGamePlayer(p, buffer);
    }
  }

  private void writeGamePlayer(GamePlayer player, FriendlyByteBuf buffer){
    buffer.writeUUID(player.getId());
    buffer.writeUtf(player.getDisplayName());
    buffer.writeUtf(player.getRole());
  }

  private GamePlayer readGamePlayer(FriendlyByteBuf buf){
    return new GamePlayer(buf.readUUID(), buf.readUtf(), buf.readUtf());
  }


  public void readPayload(FriendlyByteBuf buffer) {
    id = buffer.readUtf();
    int size = buffer.readInt();
    playerList = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      playerList.add(readGamePlayer(buffer));
    }
  }

  @Override
  public PacketType<?> getType() {
    return TYPE;
  }
}
