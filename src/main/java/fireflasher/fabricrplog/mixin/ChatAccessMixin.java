package fireflasher.fabricrplog.mixin;



import com.mojang.authlib.GameProfile;
import fireflasher.fabricrplog.ChatLogger;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
public abstract class ChatAccessMixin {
    protected String content;

    @Inject(method = "onChatMessage", at = @At("INVOKE"), cancellable = true)
    private void onChatMessage(SignedMessage message, GameProfile sender, MessageType.Parameters params, CallbackInfo ci)  {
        Text textContent = message.getContent();
        ChatLogger.chatFilter(message.toString());
    }
}
