package fireflasher.fabricrplog.config;

import fireflasher.fabricrplog.client.FabricrplogClient;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import java.io.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DefaultConfig {

    private File ConfigFile;
    private List<String> Keywords = new ArrayList<>();
    private final String ModsDir = FabricrplogClient.getModsFolder();
    private static final String info = "#Füge hier die Schlüsselwörter für den Filter pro Zeile ein";

    private static final Logger LOGGER = LogManager.getLogger("FabricRPLog Config");


    public void reloadConfig() {
        if (this.ConfigFile == null || !this.ConfigFile.exists()) {
            this.ConfigFile = new File(ModsDir, "config.yml");
            try {
                ConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Keywords.clear();
        try {
            Scanner sc = new Scanner(ConfigFile);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if( !line.startsWith("#")){
                    Keywords.add(line);
                }

            }

            if (Keywords.isEmpty()) {
                Keywords.add("[Flüstern]");
                Keywords.add("[Leise]");
                Keywords.add("[Reden]");
                Keywords.add("[Rufen]");
                Keywords.add("[PRufen]");
                Keywords.add("[Schreien]");
            }

            BufferedReader br = new BufferedReader(new FileReader(ConfigFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(ConfigFile, true));
            if (br.lines().toList().isEmpty()) bw.append(info).close();

        } catch (FileNotFoundException e)  {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<String> getList() {
        if (Keywords.isEmpty()) {
            reloadConfig();
        }
        return this.Keywords;
    }

    public void saveConfig() {
        if (this.ConfigFile == null) {
            return;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(ConfigFile, true));
            BufferedReader br = new BufferedReader(new FileReader(ConfigFile));
            br.read();

            if (br.lines().toList().isEmpty()) bw.append(info);
            for (String write : this.Keywords) {
                bw.append("\n" + write);
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setup() {
        new File(ModsDir).mkdir();
        this.ConfigFile = new File(ModsDir + "config.yml");
        if (ConfigFile.exists()) {
            LOGGER.info("[RPLog] Config erfolgreich geladen");
            reloadConfig();
        } else {
            reloadConfig();
            saveConfig();
        }
    }

    protected void openConfigFile() {Util.getOperatingSystem().open(ConfigFile);}

}

