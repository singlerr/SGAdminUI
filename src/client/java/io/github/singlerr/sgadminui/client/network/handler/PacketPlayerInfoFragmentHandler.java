package io.github.singlerr.sgadminui.client.network.handler;

import icyllis.modernui.mc.fabric.MuiFabricApi;
import io.github.singlerr.sgadminui.client.GameInfo;
import io.github.singlerr.sgadminui.client.SGAdminUIClient;
import io.github.singlerr.sgadminui.client.network.PacketBuilder;
import io.github.singlerr.sgadminui.client.network.PacketPlayerInfoFragment;
import io.github.singlerr.sgadminui.client.ui.GameInfoFragment;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;

@Slf4j
public final class PacketPlayerInfoFragmentHandler implements
    ClientPlayNetworking.PlayPacketHandler<PacketPlayerInfoFragment> {

  private final Map<Integer, PacketBuilder> buffers = new HashMap<>();

  @Override
  public void receive(PacketPlayerInfoFragment pkt, LocalPlayer localPlayer,
                      PacketSender packetSender) {
    PacketBuilder builder;
    if (buffers.containsKey(pkt.getPacketId())) {
      builder = buffers.get(pkt.getPacketId());
    } else {
      builder = new PacketBuilder(pkt.getPacketId(), pkt.getPacketCount());
      buffers.put(pkt.getPacketId(), builder);
    }

    if (builder.accept(pkt)) {
      GameInfo.Builder mainBuilder = SGAdminUIClient.beginOrGet();
      boolean shouldOpenUI =
          mainBuilder.setPlayerList(new GameInfo.PlayerList(pkt.getId(), builder.getData()));
      if (shouldOpenUI) {
        MuiFabricApi.openScreen(new GameInfoFragment(mainBuilder.build()));
      }
      buffers.remove(pkt.getPacketId());
    }

  }
}
