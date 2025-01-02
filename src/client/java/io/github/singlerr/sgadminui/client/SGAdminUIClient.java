package io.github.singlerr.sgadminui.client;

import com.mojang.blaze3d.platform.InputConstants;
import icyllis.modernui.mc.fabric.MuiFabricApi;
import io.github.singlerr.sgadminui.client.network.PacketGameInfo;
import io.github.singlerr.sgadminui.client.network.PacketPlayerInfoFragment;
import io.github.singlerr.sgadminui.client.network.PacketRequestInfo;
import io.github.singlerr.sgadminui.client.network.handler.PacketGameInfoHandler;
import io.github.singlerr.sgadminui.client.network.handler.PacketPlayerInfoFragmentHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public final class SGAdminUIClient implements ClientModInitializer {

  private static GameInfo.Builder builder = null;
  public static final KeyMapping OPEN_MENU =
      new KeyMapping("Open UI", InputConstants.KEY_B, "UI");
  @Override
  public void onInitializeClient() {
    KeyBindingHelper.registerKeyBinding(OPEN_MENU);
    ClientPlayNetworking.registerGlobalReceiver(PacketGameInfo.TYPE, new PacketGameInfoHandler());
    ClientPlayNetworking.registerGlobalReceiver(PacketPlayerInfoFragment.TYPE, new PacketPlayerInfoFragmentHandler());
    ClientTickEvents.START_CLIENT_TICK.register(new ClientTickEvents.StartTick() {
      @Override
      public void onStartTick(Minecraft client) {
        if (OPEN_MENU.isDown()) {
          ClientPlayNetworking.send(new PacketRequestInfo());
        }
      }
    });
  }

  public static GameInfo.Builder beginOrGet(){
    if(builder != null)
      return builder;
    return (builder = new GameInfo.Builder());
  }
}
