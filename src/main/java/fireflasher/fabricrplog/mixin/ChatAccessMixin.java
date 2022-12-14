package fireflasher.fabricrplog.mixin;

import fireflasher.fabricrplog.ChatLogger;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
public abstract class ChatAccessMixin {
    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(SignedMessage message, MessageType.Parameters params, CallbackInfo ci)  {
        String content = message.signedBody().content().plain();
        ChatLogger.chatFilter(content);
    }
}
