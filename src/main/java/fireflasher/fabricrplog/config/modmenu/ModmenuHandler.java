package fireflasher.fabricrplog.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModmenuHandler implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        /*
        return screen -> new Screen(Text.of("")) {
            @Override
            protected void init() {
                FabricrplogClient.CONFIG.openConfigFile();
                client.openScreen(screen);
            }
        };

         */
        return Optionsscreen::new;
    }
}
