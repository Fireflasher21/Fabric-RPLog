package fireflasher.fabricrplog.config.modmenu;

import fireflasher.fabricrplog.ChatLogger;
import fireflasher.fabricrplog.client.FabricrplogClient;
import fireflasher.fabricrplog.config.DefaultConfig;
import fireflasher.fabricrplog.config.json.ServerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class Optionsscreen extends Screen {

    private final Screen previous;
    private EntryListWidget buttonList;
    static final int CLICKABLEWIDGETHEIGHT = 20;
    private final ServerConfig dummy = new ServerConfig("dummy", List.of("dummy"), List.of("dummy"));

   public Optionsscreen(Screen previous) {
        super(Text.translatable("rplog.config.optionscreen.title"));
        this.previous = previous;
    }


    protected void init() {
        int i = 30;
        DefaultConfig defaultConfig = FabricrplogClient.CONFIG;
        List<ServerConfig> serverConfigList = defaultConfig.getList();
        if (serverConfigList.isEmpty()) {
            serverConfigList.add(dummy);
        }
        for (ServerConfig server : serverConfigList) {
            i = i + 25;
            ButtonWidget serverNameButton = ButtonWidget.builder(Text.of(ChatLogger.getServerNameShortener(server.getServerDetails().getServerNames())),
                    button ->{
                        MinecraftClient.getInstance().setScreen(new Serverscreen(MinecraftClient.getInstance().currentScreen, server));
                    }).dimensions(this.width / 2 - this.width / 4 - 50, i, 100, CLICKABLEWIDGETHEIGHT).build();

            ButtonWidget delete = ButtonWidget.builder(Text.translatable("rplog.config.screen.delete"),
                    button -> {
                        MinecraftClient.getInstance().setScreen(new Verification(MinecraftClient.getInstance().currentScreen, defaultConfig, server));
                    }).dimensions(this.width / 2 + this.width / 4 - serverNameButton.getWidth() / 2, i, serverNameButton.getWidth(), CLICKABLEWIDGETHEIGHT).build();

            if (!serverConfigList.contains(dummy)) {
                this.addDrawableChild(serverNameButton);
                this.addDrawableChild(delete);
            }
        }
        serverConfigList.remove(dummy);

        ButtonWidget addServer = ButtonWidget.builder(Text.translatable("rplog.config.optionscreen.add_Server"),
                button -> {
                    if (MinecraftClient.getInstance().getNetworkHandler() == null || MinecraftClient.getInstance().getNetworkHandler().getConnection().isLocal()) {
                    } else {
                        String address = MinecraftClient.getInstance().getNetworkHandler().getConnection().getAddress().toString();
                        Pattern serverAddress = Pattern.compile("static.([0-9]{1,3}[.]){4}");
                        String serverName;
                        Boolean ipMatcher = serverAddress.matcher(address.split("/")[0]).find();
                        String ip = address.split("/")[1];
                        ip = ip.split(":")[0];
                        if (ipMatcher) serverName = ip;
                        else serverName = address.split("/")[0];
                        defaultConfig.addServerToList(ip, serverName);
                        defaultConfig.loadConfig();
                        MinecraftClient.getInstance().setScreen(new Optionsscreen(previous));
                    }
                }).dimensions(this.width / 2 - this.width / 4 - 50, 13, 100, CLICKABLEWIDGETHEIGHT).build();



                ButtonWidget defaultconfigbutton = ButtonWidget.builder(Text.translatable("rplog.config.screen.defaults"),
                button -> {
                    ServerConfig defaults = new ServerConfig("Defaults", List.of("Defaults"), FabricrplogClient.CONFIG.getKeywords());
                    MinecraftClient.getInstance().setScreen(new Serverscreen(MinecraftClient.getInstance().currentScreen, defaults));
                }).dimensions(this.width / 2 + this.width / 4 - 50, 13, 100, CLICKABLEWIDGETHEIGHT).build();


        ButtonWidget done = ButtonWidget.builder(Text.translatable("rplog.config.screen.done"),
                button -> {
                    close();
                    defaultConfig.loadConfig();
                }).dimensions(this.width / 2 + this.width / 4 - 50, this.height - 30, 100, CLICKABLEWIDGETHEIGHT).build();

        ButtonWidget openFolder = ButtonWidget.builder(Text.translatable("rplog.config.optionscreen.open_LogFolder"),
                button -> {
                    Util.getOperatingSystem().open(new File(FabricrplogClient.getFolder()));
                }).dimensions(this.width / 2 - this.width / 4 - 50, this.height - 30, 100, CLICKABLEWIDGETHEIGHT).build();

        this.addDrawableChild(defaultconfigbutton);
        this.addDrawableChild(addServer);
        this.addDrawableChild(done);
        this.addDrawableChild(openFolder);

    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        Text serverlist = Text.translatable("rplog.config.optionscreen.configuration_Servers");
        Text deleteServer = Text.translatable("rplog.config.optionscreen.delete_Servers");
        this.renderBackground(drawContext);
        drawContext.drawTextWithShadow(this.textRenderer, this.title, this.width / 2, 18, 0xffffff);
        drawContext.drawTextWithShadow(this.textRenderer, serverlist, this.width / 2 - this.width / 4, 40, 0xffffff);
        drawContext.drawTextWithShadow(this.textRenderer, deleteServer, this.width / 2 + this.width / 4, 40, 0xffffff);
        super.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        FabricrplogClient.CONFIG.loadConfig();
        this.client.setScreen(previous);
    }


    public class Verification extends Screen {

        private final Screen previous;
        private final DefaultConfig defaultConfig;
        private final ServerConfig serverConfig;

        Verification(Screen previous, DefaultConfig defaultConfig, ServerConfig serverConfig){
            super(Text.of(""));
            this.previous = previous;
            this.defaultConfig = defaultConfig;
            this.serverConfig = serverConfig;
        }

        public void init() {
            ButtonWidget delete = ButtonWidget.builder(Text.translatable("rplog.config.optionscreen.verification.delete"),
                    button -> {
                        defaultConfig.removeServerFromList(serverConfig);
                        super.close();
                        MinecraftClient.getInstance().setScreen(new Optionsscreen(previous));
                    }).dimensions(this.width / 2 - this.width / 4 - 50, this.height / 2, 100, CLICKABLEWIDGETHEIGHT).build();


            ButtonWidget abort = ButtonWidget.builder(Text.translatable("rplog.config.optionscreen.verification.cancel"),
                    button -> {
                        close();
                    }).dimensions(this.width / 2 + this.width / 4 - 50, this.height / 2, 100, CLICKABLEWIDGETHEIGHT).build();

            this.addDrawableChild(delete);
            this.addDrawableChild(abort);

        }

        public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
            Text verificationmessage = Text.translatable("rplog.config.optionscreen.verification.message");
            this.renderBackground(drawContext);
            drawContext.drawCenteredTextWithShadow(this.textRenderer, verificationmessage, this.width / 2, this.height / 2 - this.height / 4, 0xffffff);
            super.render(drawContext, mouseX, mouseY, delta);
        }

        @Override
        public void close(){
            this.client.setScreen(previous);
        }

    }
}