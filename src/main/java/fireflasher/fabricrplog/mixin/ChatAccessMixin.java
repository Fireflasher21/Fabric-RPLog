package fireflasher.fabricrplog.mixin;

import com.mojang.authlib.GameProfile;
import fireflasher.fabricrplog.ChatLogger;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;
import java.util.logging.Logger;

@Mixin(MessageHandler.class)
public abstract class ChatAccessMixin {
    @Inject(method = "addToChatLog(Lnet/minecraft/text/Text;Ljava/time/Instant;)V", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(Text message, Instant timestamp, CallbackInfo ci)  {
        String content = message.getString();
        ChatLogger.chatFilter(content);
    }
}
