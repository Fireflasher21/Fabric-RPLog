package fireflasher.fabricrplog;

import fireflasher.fabricrplog.listener.ChatAccess;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.network.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Fabricrplog implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger("FabricRPLog");

    @Override
    public void onInitialize() {



    }
}
