package fireflasher.fabricrplog.client;

import fireflasher.fabricrplog.ChatLogger;
import fireflasher.fabricrplog.config.DefaultConfig;
import fireflasher.fabricrplog.Fabricrplog;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class FabricrplogClient implements ClientModInitializer {

    public static DefaultConfig CONFIG = new DefaultConfig();
    public static ChatLogger CHATLOGGER;
    public static Logger LOGGER = Fabricrplog.LOGGER;

    @Override
    public void onInitializeClient() {
        CONFIG.setup();
        CHATLOGGER = new ChatLogger();
        CHATLOGGER.setup();

    }

    public static String getFolder(){ return FabricLoader.getInstance().getGameDir().toString() + "/RPLogs/";}
    public static String getModsFolder(){ return FabricLoader.getInstance().getConfigDir().toString() + "/";}



}
