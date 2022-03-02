package fireflasher.fabricrplog.config;

import fireflasher.fabricrplog.client.FabricrplogClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;


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
        this.done = new ClickableWidget(openfile.getZOffset() + 250, 30, openfile.getWidth(), 20, Text.of("Done")) {
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
        String warning_en = "Keep in Mind to save the config File before clicking done, bc the changes would not get saved";
        String warning_de_1 = "Speicher zuerst die Datei, bevor du auf Done klickst,";
        String warning_de_2 = "da die Änderungen sonst nicht übernommen werden";
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        drawCenteredText(matrices,this.textRenderer, Text.of(warning_de_1), done.getZOffset() + 250, done.getHeight() + 50,0xaaaaaa );
        drawCenteredText(matrices,this.textRenderer, Text.of(warning_de_2), done.getZOffset() + 250, done.getHeight() + 60,0xaaaaaa );
        super.render(matrices, mouseX, mouseY, delta);
    }
}
