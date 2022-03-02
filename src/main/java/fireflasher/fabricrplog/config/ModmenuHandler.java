package fireflasher.fabricrplog.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fireflasher.fabricrplog.client.FabricrplogClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModmenuHandler implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new Screen(Text.of("")) {
            @Override
            protected void init() {
                FabricrplogClient.CONFIG.openConfigFile();
                client.openScreen(screen);
            }
        };
    }
}
