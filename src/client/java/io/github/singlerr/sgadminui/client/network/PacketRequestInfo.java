package io.github.singlerr.sgadminui.client.network;


import lombok.NoArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@NoArgsConstructor
public final class PacketRequestInfo implements FabricPacket {

  public static final PacketType<PacketRequestInfo> TYPE = PacketType.create(new ResourceLocation("sgadmin", "request_info"), PacketRequestInfo::new);

  public PacketRequestInfo(FriendlyByteBuf buf){
    readPayload(buf);
  }

  @Override
  public void write(FriendlyByteBuf buffer) {
    buffer.writeInt(0);
  }

  public void readPayload(FriendlyByteBuf buffer) {

  }

  @Override
  public PacketType<?> getType() {
    return TYPE;
  }
}
