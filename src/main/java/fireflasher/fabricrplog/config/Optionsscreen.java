package fireflasher.fabricrplog.config;

import fireflasher.fabricrplog.client.FabricrplogClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.checkerframework.checker.units.qual.C;

public class Optionsscreen extends GameOptionsScreen {

    private Screen previous;
    private ClickableWidget openfile;
    private ClickableWidget savefile;
    private ClickableWidget done;

    public Optionsscreen(Screen previous) {
        super(previous, MinecraftClient.getInstance().options, Text.of("Fabricrplog options"));
        this.previous = previous;
    }

    protected void init() {

        this.openfile = new ClickableWidget(getZOffset() + 30, 30, 128, 20, Text.of("Open Config")) {
            @Override
            public void appendNarrations(NarrationMessageBuilder builder) {
                return;
            }

            @Override
            public void onClick(double mouseX, double mouseY) {
                FabricrplogClient.CONFIG.openConfigFile();
            }
        };
        this.savefile = new ClickableWidget(150, 30, 128, 20, Text.of("Reload Config")) {
            @Override
            public void appendNarrations(NarrationMessageBuilder builder) {
                return;
            }

            @Override
            public void onClick(double mouseX, double mouseY) {
                FabricrplogClient.CONFIG.reloadConfig();
            }
        };
        this.done = new ClickableWidget(280 , 30, 128, 20, Text.of("Done")) {
                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    return;
                }

                @Override
                public void onClick(double mouseX, double mouseY) {
                    onClose();
                    FabricrplogClient.CONFIG.reloadConfig();
                }
            };

        this.addDrawableChild(openfile);
        //this.addDrawableChild(savefile);
        this.addDrawableChild(done);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
