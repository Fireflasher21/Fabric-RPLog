package fireflasher.fabricrplog.listener;

import net.minecraft.client.gui.ClientChatListener;
import net.minecraft.network.MessageType;

public interface ChatAccess {
    void registerChatListener(MessageType messageType, ClientChatListener listener);
}
