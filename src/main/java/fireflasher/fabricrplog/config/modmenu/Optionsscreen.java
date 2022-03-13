package fireflasher.fabricrplog.config.modmenu;

import fireflasher.fabricrplog.ChatLogger;
import fireflasher.fabricrplog.client.FabricrplogClient;
import fireflasher.fabricrplog.config.DefaultConfig;
import fireflasher.fabricrplog.config.json.ServerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class Optionsscreen extends GameOptionsScreen {

    private Screen previous;
    static final int CLICKABLEWIDGETHEIGHT = 20;
    private final ServerConfig dummy = new ServerConfig("dummy", List.of("dummy"), List.of("dummy"));

    Optionsscreen(Screen previous) {
        super(previous, MinecraftClient.getInstance().options, Text.of("RPlog Options"));
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
                public void onClick(double mouseX, double mouseY) {
                    MinecraftClient.getInstance().openScreen(new Serverscreen(MinecraftClient.getInstance().currentScreen, server));
                }
            };


            ClickableWidget delete = new ClickableWidget(this.width / 2 + this.width / 4 - button.getWidth() / 2, i, button.getWidth(), CLICKABLEWIDGETHEIGHT, Text.of("Löschen")) {

                @Override
                public void onClick(double mouseX, double mouseY) {
                    MinecraftClient.getInstance().openScreen(new Verification(MinecraftClient.getInstance().currentScreen, defaultConfig, server));
                }
            };

            if (!serverConfigList.contains(dummy)) {
                this.addButton(button);
                this.addButton(delete);
            }
        }
        serverConfigList.remove(dummy);

        ClickableWidget addServer = new ClickableWidget(this.width / 2 - this.width / 4 - 50, 30, 100, CLICKABLEWIDGETHEIGHT, Text.of("Server Hinzufügen")) {

            @Override
            public void onClick(double mouseX, double mouseY) {
                if (MinecraftClient.getInstance().getNetworkHandler() == null || MinecraftClient.getInstance().getNetworkHandler().getConnection().isLocal()) {
                } else {
                    String address = MinecraftClient.getInstance().getNetworkHandler().getConnection().getAddress().toString();
                    String ip = address.toString().split("/")[1];
                    String servername = address.toString().split("/")[0];
                    ip = ip.split(":")[0];
                    defaultConfig.addServerToList(ip, servername);
                    MinecraftClient.getInstance().openScreen(new Optionsscreen(previous));
                }
            }
        };


        ClickableWidget done = new ClickableWidget(this.width / 2 + this.width / 4 - addServer.getWidth() / 2, 30, addServer.getWidth(), CLICKABLEWIDGETHEIGHT, Text.of("Done")) {

            @Override
            public void onClick(double mouseX, double mouseY) {
                onClose();
                defaultConfig.loadConfig();
            }
        };

        ClickableWidget defaultconfigbutton = new ClickableWidget(this.width / 2 + - 30 , 30, 60, CLICKABLEWIDGETHEIGHT, Text.of("Defaults")) {

            @Override
            public void onClick(double mouseX, double mouseY) {
                ServerConfig defaults = new ServerConfig("Defaults",List.of("Defaults"),FabricrplogClient.CONFIG.getKeywords());
                MinecraftClient.getInstance().openScreen(new Serverscreen(MinecraftClient.getInstance().currentScreen, defaults));
            }
        };

        this.addButton(defaultconfigbutton);
        this.addButton(addServer);
        this.addButton(done);

    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        String serverlist = "Konfigurierbare Server";
        String deleteServer = "Server löschen";
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        drawCenteredText(matrices, this.textRenderer, Text.of(serverlist), this.width / 2 - this.width / 4, 60, 0xffffff);
        drawCenteredText(matrices, this.textRenderer, Text.of(deleteServer), this.width / 2 + this.width / 4, 60, 0xffffff);
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
            ClickableWidget delete = new ClickableWidget(this.width / 2 - this.width / 4 - 50, this.height / 2, 100, CLICKABLEWIDGETHEIGHT, Text.of("Ja")) {

                @Override
                public void onClick(double mouseX, double mouseY) {
                    defaultConfig.removeServerFromList(serverConfig);
                    MinecraftClient.getInstance().openScreen(new Optionsscreen(previous));
                }
            };


            ClickableWidget abort = new ClickableWidget(this.width / 2 + this.width / 4 - 50, this.height / 2,100, CLICKABLEWIDGETHEIGHT, Text.of("Nein")) {

                @Override
                public void onClick(double mouseX, double mouseY) {
                    onClose();
                }
            };

            this.addButton(delete);
            this.addButton(abort);

        }

        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            this.renderBackground(matrices);
            drawCenteredText(matrices, this.textRenderer, Text.of("Bist du sicher, dass du den Server löschen willst?"), this.width / 2, this.height / 2 - this.height / 4, 0xffffff);
            super.render(matrices, mouseX, mouseY, delta);
        }

        @Override
        public void onClose(){
            this.client.openScreen(previous);
        }

    }
}