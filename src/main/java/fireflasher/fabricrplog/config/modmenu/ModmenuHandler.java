package fireflasher.fabricrplog.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.gui.ModsScreen;
import fireflasher.fabricrplog.config.json.ServerConfig;
import fireflasher.fabricrplog.config.modmenu.Optionsscreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModmenuHandler implements ModMenuApi  {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return Optionsscreen::new;
    }
}
