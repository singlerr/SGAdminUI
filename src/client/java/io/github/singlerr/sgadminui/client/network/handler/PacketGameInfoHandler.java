package io.github.singlerr.sgadminui.client.network.handler;

import io.github.singlerr.sgadminui.client.GameInfo;
import io.github.singlerr.sgadminui.client.SGAdminUIClient;
import io.github.singlerr.sgadminui.client.network.PacketGameInfo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;

public final class PacketGameInfoHandler
    implements ClientPlayNetworking.PlayPacketHandler<PacketGameInfo> {
  @Override
  public void receive(PacketGameInfo packetGameInfo, LocalPlayer localPlayer,
                      PacketSender packetSender) {
    GameInfo.Builder builder = SGAdminUIClient.beginOrGet();
    builder.setGame(
        new GameInfo.Game(packetGameInfo.getCurrentGameId(), packetGameInfo.getCurrentGameInfo(),
            packetGameInfo.getGames()));
  }
}
