package fireflasher.fabricrplog.config.modmenu;

import fireflasher.fabricrplog.ChatLogger;
import fireflasher.fabricrplog.client.FabricrplogClient;
import fireflasher.fabricrplog.config.DefaultConfig;
import fireflasher.fabricrplog.config.json.ServerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

import static fireflasher.fabricrplog.config.modmenu.Optionsscreen.CLICKABLEWIDGETHEIGHT;

class Serverscreen extends Screen {

    private final Screen previous;
    private final ServerConfig serverConfig;

    Serverscreen(Screen previous, ServerConfig serverConfig) {
        super(Text.of(ChatLogger.getServerNameShortener(serverConfig.getServerDetails().getServerNames())));
        this.previous = previous;
        this.serverConfig = serverConfig;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.init(client, width, height);
    }

    @Override
    protected void init() {


        ServerConfig.ServerDetails serverDetails = serverConfig.getServerDetails();
        List<String> keywords = serverDetails.getServerKeywords();

        int i = 30;
        ButtonWidget reset = ButtonWidget.builder(Text.translatable("rplog.config.serverscreen.reset_defaults"), button -> {
                serverConfig.getServerDetails().getServerKeywords().clear();
                serverConfig.getServerDetails().getServerKeywords().addAll(FabricrplogClient.CONFIG.getKeywords());
                MinecraftClient.getInstance().setScreen(new Serverscreen(previous, serverConfig));
            }).dimensions(this.width / 2 - this.width / 4 - 50, 13, 100, CLICKABLEWIDGETHEIGHT).build();

        ButtonWidget done = ButtonWidget.builder(Text.translatable("rplog.config.screen.done"),
                button -> {
                FabricrplogClient.CONFIG.saveConfig();
                close();
            }).dimensions(this.width / 2 + this.width / 4 - reset.getWidth() / 2 , 13, reset.getWidth(), CLICKABLEWIDGETHEIGHT).build();

        for (String keyword : keywords) {
            i = i + 20;
            ButtonWidget delete = ButtonWidget.builder(Text.translatable("rplog.config.screen.delete"),
                    button -> {
                    keywords.remove(keyword);
                    serverConfig.setServerDetails(serverDetails);
                    MinecraftClient.getInstance().setScreen(new Serverscreen(previous, serverConfig));
                }).dimensions(this.width / 2 + this.width / 4 - reset.getWidth() / 2, i - 5, reset.getWidth(), CLICKABLEWIDGETHEIGHT).build();

            this.addDrawableChild(delete);
        }

        i = i + 20;
        TextFieldWidget insert = new TextFieldWidget(textRenderer, this.width / 2 - this.width / 4 - 50, i, 100, CLICKABLEWIDGETHEIGHT, Text.of("Keyword")) {};

        ButtonWidget add = ButtonWidget.builder(Text.translatable("rplog.config.serverscreen.add_Keywords"),
                button -> {

                if(keywords.contains(insert.getText()));
                else if (!insert.getText().isEmpty()) {
                    keywords.add(insert.getText());
                    serverConfig.setServerDetails(serverDetails);
                    MinecraftClient.getInstance().setScreen(new Serverscreen(previous, serverConfig));
                }}).dimensions(this.width / 2 + this.width / 4 - insert.getWidth() / 2, i, insert.getWidth(), CLICKABLEWIDGETHEIGHT).build();

        this.addDrawableChild(done);
        this.addDrawableChild(reset);
        this.addDrawableChild(insert);
        this.addDrawableChild(add);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        drawContext.drawTextWithShadow(this.textRenderer, this.title, this.width / 2, 18, 0xffffff);
        List<String> keywords = serverConfig.getServerDetails().getServerKeywords();
        int i = 30;
        for(String keyword:keywords){
            i = i + 20;
            drawContext.drawTextWithShadow(this.textRenderer, Text.of(keyword), this.width / 2 - this.width / 4 , i ,0xffffff);
        }
        super.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public void close(){
        this.client.setScreen(previous);
    }


}


