package io.github.singlerr.sgadminui.client.network.handler;

import icyllis.modernui.mc.fabric.MuiFabricApi;
import io.github.singlerr.sgadminui.client.GameInfo;
import io.github.singlerr.sgadminui.client.SGAdminUIClient;
import io.github.singlerr.sgadminui.client.network.PacketPlayerInfoFragment;
import io.github.singlerr.sgadminui.client.ui.GameInfoUI;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;

public final class PacketPlayerInfoFragmentHandler implements
    ClientPlayNetworking.PlayPacketHandler<PacketPlayerInfoFragment> {
  @Override
  public void receive(PacketPlayerInfoFragment packetPlayerInfoFragment, LocalPlayer localPlayer,
                      PacketSender packetSender) {
    GameInfo.Builder builder = SGAdminUIClient.beginOrGet();
    boolean shouldOpenUI = builder.setPlayerList(new GameInfo.PlayerList(
        packetPlayerInfoFragment.getId(), packetPlayerInfoFragment.getPlayerList()));
    if(shouldOpenUI){
      MuiFabricApi.openScreen(new GameInfoUI(builder.build()));
    }
  }
}
