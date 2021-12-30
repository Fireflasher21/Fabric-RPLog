package fireflasher.fabricrplog.client;

import fireflasher.fabricrplog.ChatLogger;
import fireflasher.fabricrplog.DefaultConfig;
import fireflasher.fabricrplog.listener.ChatAccess;
import fireflasher.fabricrplog.mixin.ChatAccessMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

@Environment(EnvType.CLIENT)
public class FabricrplogClient implements ClientModInitializer {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {


        new DefaultConfig().setup();
        ChatLogger.setup();

        chatregister();
    }

    private void chatregister(){
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {


            ((ChatAccess)client.inGameHud).registerChatListener(
                    MessageType.CHAT,
                    (type, message, sender) -> {
                        ChatLogger.chatFilter(message.getString());
                    });
            /*
            ((ChatAccess)client.inGameHud).registerChatListener(
                    MessageType.SYSTEM,
                    (type, message, sender) -> {
                        ChatLogger.chatFilter(message.getString());
                    });

            ((ChatAccess)client.inGameHud).registerChatListener(
                    MessageType.GAME_INFO,
                    (type, message, sender) -> {
                        ChatLogger.chatFilter(message.getString());
                    });

             */
        });
    }

    public static String getFolder(){ return FabricLoader.getInstance().getGameDir().toString();}
    public static String getModsFolder(){ return FabricLoader.getInstance().getConfigDir().toString() + "/RPLog/";}



}
