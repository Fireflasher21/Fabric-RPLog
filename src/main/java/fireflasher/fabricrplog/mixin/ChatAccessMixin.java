package fireflasher.fabricrplog.mixin;


import fireflasher.fabricrplog.ChatLogger;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatHudListener.class)
public abstract class ChatAccessMixin {

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    public void onChatMessage(MessageType type, Text message, UUID sender, CallbackInfo ci) {
        ChatLogger.chatFilter(message.asString());
    }
}
