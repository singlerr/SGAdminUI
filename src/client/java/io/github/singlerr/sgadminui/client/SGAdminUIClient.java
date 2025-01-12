package io.github.singlerr.sgadminui.client;

import com.mojang.blaze3d.platform.InputConstants;
import icyllis.modernui.graphics.text.FontFamily;
import icyllis.modernui.text.Typeface;
import io.github.singlerr.sgadminui.client.network.PacketGameInfo;
import io.github.singlerr.sgadminui.client.network.PacketPlayerInfoFragment;
import io.github.singlerr.sgadminui.client.network.PacketRequestInfo;
import io.github.singlerr.sgadminui.client.network.handler.PacketGameInfoHandler;
import io.github.singlerr.sgadminui.client.network.handler.PacketPlayerInfoFragmentHandler;
import java.io.InputStream;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public final class SGAdminUIClient implements ClientModInitializer {

  public static final KeyMapping OPEN_MENU =
      new KeyMapping("Open UI", InputConstants.KEY_B, "UI");
  public static Typeface NANUM_FONT;
  private static GameInfo.Builder builder = null;

  public static GameInfo.Builder beginOrGet() {
    if (builder != null) {
      return builder;
    }
    return (builder = new GameInfo.Builder());
  }

  @Override
  public void onInitializeClient() {
    ResourceManagerHelperImpl.get(PackType.CLIENT_RESOURCES).registerReloadListener(
        new SimpleSynchronousResourceReloadListener() {
          @Override
          public ResourceLocation getFabricId() {
            return new ResourceLocation("sgadminui", "client_resources");
          }

          @Override
          public void onResourceManagerReload(ResourceManager resourceManager) {
            try (InputStream in = resourceManager
                .open(new ResourceLocation("sgadminui", "font/nanum_square_neo.ttf"))) {
              FontFamily font = FontFamily.createFamily(in, false);
              NANUM_FONT = Typeface.createTypeface(font);

            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        });
    ClientLifecycleEvents.CLIENT_STARTED.register((mc) -> {
      try (InputStream in = mc.getResourceManager()
          .open(new ResourceLocation("sgadminui", "font/nanum_square_neo.ttf"))) {
        FontFamily font = FontFamily.createFamily(in, false);
        NANUM_FONT = Typeface.createTypeface(font);

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    KeyBindingHelper.registerKeyBinding(OPEN_MENU);
    ClientPlayNetworking.registerGlobalReceiver(PacketGameInfo.TYPE, new PacketGameInfoHandler());
    ClientPlayNetworking.registerGlobalReceiver(PacketPlayerInfoFragment.TYPE,
        new PacketPlayerInfoFragmentHandler());
    ClientTickEvents.START_CLIENT_TICK.register(client -> {
      if (OPEN_MENU.isDown()) {
        ClientPlayNetworking.send(new PacketRequestInfo());
      }
    });
  }
}
