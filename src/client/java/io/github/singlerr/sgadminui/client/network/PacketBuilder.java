package io.github.singlerr.sgadminui.client.network;

import io.github.singlerr.sgadminui.client.GamePlayer;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PacketBuilder {
  private final int id;
  private final boolean[] states;

  @Getter
  private final List<GamePlayer> data;

  public PacketBuilder(int id, int count) {
    this.id = id;
    this.states = new boolean[count];
    this.data = new ArrayList<>();
  }

  private boolean validate() {
    boolean result = true;
    for (boolean state : states) {
      result &= state;
    }

    return result;
  }

  public boolean accept(PacketPlayerInfoFragment packet) {
    if (packet.getPacketId() != id) {
      return false;
    }
    if(states.length == 0)
      return true;
    if (packet.getPacketIndex() >= states.length) {
      return false;
    }
    if (states[packet.getPacketIndex()]) {
      return false;
    }
    states[packet.getPacketIndex()] = true;
    data.addAll(packet.getPlayerList());

    return validate();
  }
}
