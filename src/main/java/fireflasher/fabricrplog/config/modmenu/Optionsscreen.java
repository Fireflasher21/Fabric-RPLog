package fireflasher.fabricrplog.config.modmenu;

import fireflasher.fabricrplog.ChatLogger;
import fireflasher.fabricrplog.client.FabricrplogClient;
import fireflasher.fabricrplog.config.DefaultConfig;
import fireflasher.fabricrplog.config.json.ServerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class Optionsscreen extends Screen {

    private Screen previous;
    static final int CLICKABLEWIDGETHEIGHT = 20;
    private final ServerConfig dummy = new ServerConfig("dummy", List.of("dummy"), List.of("dummy"));

    Optionsscreen(Screen previous) {
        super(new TranslatableText("rplog.config.optionscreen.title"));
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
            ButtonWidget serverNameButton = new ButtonWidget(this.width / 2 - this.width / 4 - 50, i, 100, CLICKABLEWIDGETHEIGHT, Text.of(ChatLogger.getServerNameShortener(server.getServerDetails().getServerNames())),
            button ->{
                    MinecraftClient.getInstance().openScreen(new Serverscreen(MinecraftClient.getInstance().currentScreen, server));
                });



            ButtonWidget delete = new ButtonWidget(this.width / 2 + this.width / 4 - serverNameButton.getWidth() / 2, i, serverNameButton.getWidth(), CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.screen.delete"),
                    button -> {
                    MinecraftClient.getInstance().openScreen(new Verification(MinecraftClient.getInstance().currentScreen, defaultConfig, server));
                });

            if (!serverConfigList.contains(dummy)) {
                this.addButton(serverNameButton);
                this.addButton(delete);
            }
        }
        serverConfigList.remove(dummy);

        ButtonWidget addServer = new ButtonWidget(this.width / 2 - this.width / 4 - 50, 13, 100, CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.optionscreen.add_Server"),
                button -> {
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
                    MinecraftClient.getInstance().openScreen(new Optionsscreen(previous));
                }
            });


        ButtonWidget defaultconfigbutton = new ButtonWidget(this.width / 2 + this.width / 4 - 50 , 13, 100, CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.screen.defaults"),
                button -> {
                ServerConfig defaults = new ServerConfig("Defaults",List.of("Defaults"),FabricrplogClient.CONFIG.getKeywords());
                MinecraftClient.getInstance().openScreen(new Serverscreen(MinecraftClient.getInstance().currentScreen, defaults));
            });


        ButtonWidget done = new ButtonWidget(this.width / 2 + this.width / 4 - 50, this.height - 30, 100 , CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.screen.done"),
                button -> {
                    onClose();
                    defaultConfig.loadConfig();
                });

        ButtonWidget openFolder = new ButtonWidget(this.width / 2 - this.width / 4 - 50, this.height - 30, 100 , CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.optionscreen.open_LogFolder"),
                button ->{
                    Util.getOperatingSystem().open(new File(FabricrplogClient.getFolder()));
                });

        this.addButton(defaultconfigbutton);
        this.addButton(addServer);
        this.addButton(done);
        this.addButton(openFolder);

    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TranslatableText serverlist = new TranslatableText("rplog.config.optionscreen.configuration_Servers");
        TranslatableText deleteServer = new TranslatableText("rplog.config.optionscreen.delete_Servers");
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 18, 0xffffff);
        drawCenteredText(matrices, this.textRenderer, serverlist, this.width / 2 - this.width / 4, 40, 0xffffff);
        drawCenteredText(matrices, this.textRenderer, deleteServer, this.width / 2 + this.width / 4, 40, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose(){
        FabricrplogClient.CONFIG.loadConfig();
        this.client.openScreen(null);
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
            ButtonWidget delete = new ButtonWidget(this.width / 2 - this.width / 4 - 50, this.height / 2, 100, CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.optionscreen.verification.delete"),
                    button -> {
                    defaultConfig.removeServerFromList(serverConfig);
                    super.onClose();
                    MinecraftClient.getInstance().openScreen(new Optionsscreen(previous));
                });


            ButtonWidget abort = new ButtonWidget(this.width / 2 + this.width / 4 - 50, this.height / 2,100, CLICKABLEWIDGETHEIGHT, new TranslatableText("rplog.config.optionscreen.verification.cancel"),
                    button -> {
                    onClose();
                });

            this.addButton(delete);
            this.addButton(abort);

        }

        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            TranslatableText verificationmessage = new TranslatableText("rplog.config.optionscreen.verification.message");
            this.renderBackground(matrices);
            drawCenteredText(matrices, this.textRenderer, verificationmessage, this.width / 2, this.height / 2 - this.height / 4, 0xffffff);
            super.render(matrices, mouseX, mouseY, delta);
        }

        @Override
        public void onClose(){
            this.client.openScreen(previous);
        }

    }
}