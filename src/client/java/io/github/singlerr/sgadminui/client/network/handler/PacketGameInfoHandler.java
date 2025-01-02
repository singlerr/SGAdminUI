package io.github.singlerr.sgadminui.client.network.handler;

import icyllis.modernui.mc.fabric.MuiFabricApi;
import io.github.singlerr.sgadminui.client.GameInfo;
import io.github.singlerr.sgadminui.client.SGAdminUIClient;
import io.github.singlerr.sgadminui.client.network.PacketGameInfo;
import io.github.singlerr.sgadminui.client.ui.GameInfoUI;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;

public final class PacketGameInfoHandler implements ClientPlayNetworking.PlayPacketHandler<PacketGameInfo> {
  @Override
  public void receive(PacketGameInfo packetGameInfo, LocalPlayer localPlayer,
                      PacketSender packetSender) {
    GameInfo.Builder builder = SGAdminUIClient.beginOrGet();
    boolean shouldOpenUI = builder.setGame(new GameInfo.Game(packetGameInfo.getCurrentGameId(), packetGameInfo.getGames()));
    if(shouldOpenUI){
      MuiFabricApi.openScreen(new GameInfoUI(builder.build()));
    }
  }
}
