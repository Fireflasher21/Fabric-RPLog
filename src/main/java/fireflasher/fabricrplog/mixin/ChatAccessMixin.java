package fireflasher.fabricrplog.mixin;


import fireflasher.fabricrplog.listener.ChatAccess;
import net.minecraft.client.gui.ClientChatListener;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.network.MessageType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(InGameHud.class)
public abstract class ChatAccessMixin implements ChatAccess {

    @Shadow
    @Final
    private Map<MessageType, List<ClientChatListener>> listeners;

    public void registerChatListener(MessageType messageType, ClientChatListener listener) {
        this.listeners.get(messageType).add(listener);
    }
}
