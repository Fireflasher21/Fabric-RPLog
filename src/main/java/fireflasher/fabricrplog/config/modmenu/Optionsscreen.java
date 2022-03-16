package fireflasher.fabricrplog.config.modmenu;

import fireflasher.fabricrplog.ChatLogger;
import fireflasher.fabricrplog.Fabricrplog;
import fireflasher.fabricrplog.client.FabricrplogClient;
import fireflasher.fabricrplog.config.DefaultConfig;
import fireflasher.fabricrplog.config.json.ServerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class Optionsscreen extends Screen {

    private Screen previous;
    private EntryListWidget buttonList;
    static final int CLICKABLEWIDGETHEIGHT = 20;
    private final ServerConfig dummy = new ServerConfig("dummy", List.of("dummy"), List.of("dummy"));

    Optionsscreen(Screen previous) {
        super(new TranslatableText("rplog.config.optionscreen.title"));
        this.previous = previous;
    }



    protected void init() {
        int i = 50;
        DefaultConfig defaultConfig = FabricrplogClient.CONFIG;
        List<ServerConfig> serverConfigList = defaultConfig.getList();
        if (serverConfigList.isEmpty()) {
            serverConfigList.add(dummy);
        }
        for (ServerConfig server : serverConfigList) {
            i = i + 30;
            ClickableWidget button = new ClickableWidget(this.width / 2 - this.width / 4 - 50, i, 100, CLICKABLEWIDGETHEIGHT, Text.of(ChatLogger.getServerNameShortener(server.getServerDetails().getServerNames()))) {
                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    return;
                }

                @Override
                public void onClick(double mouseX, double mouseY) {
                    MinecraftClient.getInstance().setScreen(new Serverscreen(MinecraftClient.getInstance().currentScreen, server));
                }
            };


            ClickableWidget delete = new ClickableWidget(this.width / 2 + this.width / 4 - button.getWidth() / 2, i, button.getWidth(), CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.screen.delete")) {
                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    return;
                }

                @Override
                public void onClick(double mouseX, double mouseY) {
                    MinecraftClient.getInstance().setScreen(new Verification(MinecraftClient.getInstance().currentScreen, defaultConfig, server));
                }
            };

            if (!serverConfigList.contains(dummy)) {
                this.addDrawableChild(button);
                this.addDrawableChild(delete);
            }
        }
        serverConfigList.remove(dummy);

        ClickableWidget addServer = new ClickableWidget(this.width / 2 - this.width / 4 - 50, 30, 100, CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.optionscreen.add_Server")) {
            @Override
            public void appendNarrations(NarrationMessageBuilder builder) {
                return;
            }

            @Override
            public void onClick(double mouseX, double mouseY) {
                if (MinecraftClient.getInstance().getNetworkHandler() == null || MinecraftClient.getInstance().getNetworkHandler().getConnection().isLocal()) {
                } else {
                    String address = MinecraftClient.getInstance().getNetworkHandler().getConnection().getAddress().toString();
                    Pattern serverAddress = Pattern.compile("static.([0-9]{1,3}[.]){4}");
                    String serverName;
                    Boolean ipMatcher = serverAddress.matcher(address.split("/")[0]).find();
                    String ip = address.split("/")[1];
                    ip = ip.split(":")[0];
                    if(ipMatcher) serverName = ip;
                    else serverName = address.split("/")[0];
                    defaultConfig.addServerToList(ip, serverName);
                    defaultConfig.loadConfig();
                    MinecraftClient.getInstance().setScreen(new Optionsscreen(previous));
                }
            }
        };


        ClickableWidget done = new ClickableWidget(this.width / 2 + this.width / 4 - addServer.getWidth() / 2, 30, addServer.getWidth(), CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.screen.done")) {
            @Override
            public void appendNarrations(NarrationMessageBuilder builder) {
                return;
            }

            @Override
            public void onClick(double mouseX, double mouseY) {
                onClose();
                defaultConfig.loadConfig();
            }
        };

        ClickableWidget defaultconfigbutton = new ClickableWidget(this.width / 2 + - 30 , 30, 60, CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.screen.defaults")) {
            @Override
            public void appendNarrations(NarrationMessageBuilder builder) {
                return;
            }

            @Override
            public void onClick(double mouseX, double mouseY) {
                ServerConfig defaults = new ServerConfig("Defaults",List.of("Defaults"),FabricrplogClient.CONFIG.getKeywords());
                MinecraftClient.getInstance().setScreen(new Serverscreen(MinecraftClient.getInstance().currentScreen, defaults));
            }
        };

        this.addDrawableChild(defaultconfigbutton);
        this.addDrawableChild(addServer);
        this.addDrawableChild(done);

        ButtonWidget openFolder = new ButtonWidget(this.width / 2, 100 , 60 , CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.optionscreen.open_LogFolder"),
                button ->{
                    Util.getOperatingSystem().open(new File(FabricrplogClient.getFolder()));
        });

        this.addDrawableChild(openFolder);

    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TranslatableText serverlist = new TranslatableText("rplog.config.optionscreen.configuration_Servers");
        TranslatableText deleteServer = new TranslatableText("rplog.config.optionscreen.delete_Servers");
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        drawCenteredText(matrices, this.textRenderer, serverlist, this.width / 2 - this.width / 4, 60, 0xffffff);
        drawCenteredText(matrices, this.textRenderer, deleteServer, this.width / 2 + this.width / 4, 60, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }


    public class Verification extends Screen{

        private Screen previous;
        private DefaultConfig defaultConfig;
        private ServerConfig serverConfig;

        Verification(Screen previous, DefaultConfig defaultConfig, ServerConfig serverConfig){
            super(Text.of(""));
            this.previous = previous;
            this.defaultConfig = defaultConfig;
            this.serverConfig = serverConfig;
        }

        public void init(){
            ClickableWidget delete = new ClickableWidget(this.width / 2 - this.width / 4 - 50, this.height / 2, 100, CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.optionscreen.verification.delete")) {
                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    return;
                }

                @Override
                public void onClick(double mouseX, double mouseY) {
                    defaultConfig.removeServerFromList(serverConfig);
                    Optionsscreen.super.onClose();
                    MinecraftClient.getInstance().setScreen(new Optionsscreen(previous));
                }
            };


            ClickableWidget abort = new ClickableWidget(this.width / 2 + this.width / 4 - 50, this.height / 2,100, CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.optionscreen.verification.cancel")) {
                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    return;
                }

                @Override
                public void onClick(double mouseX, double mouseY) {
                    onClose();
                }
            };

            this.addDrawableChild(delete);
            this.addDrawableChild(abort);

        }

        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            TranslatableText verificationmessage = new TranslatableText("rplog.config.optionscreen.verification.message");
            this.renderBackground(matrices);
            drawCenteredText(matrices, this.textRenderer, verificationmessage, this.width / 2, this.height / 2 - this.height / 4, 0xffffff);
            super.render(matrices, mouseX, mouseY, delta);
        }

        @Override
        public void onClose(){
            this.client.setScreen(previous);
        }

    }
}