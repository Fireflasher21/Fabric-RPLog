package fireflasher.fabricrplog.mixin;

import fireflasher.fabricrplog.config.modmenu.Optionsscreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(GameMenuScreen.class)
public abstract class PauseScreenMixin extends Screen {


    protected PauseScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = ("initWidgets"), at = @At("HEAD"))
    public void createPauseMenu(CallbackInfo callbackInfo){
        if(!FabricLoader.getInstance().isModLoaded("modmenu")) {
            ButtonWidget accessModOption = new ButtonWidget(0, 0, 35, 20, Text.of("RPL"), button -> {
                MinecraftClient.getInstance().openScreen(new Optionsscreen(this));
            });
            addButton(accessModOption);
        }
    }
}
