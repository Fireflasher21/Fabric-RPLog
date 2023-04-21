package fireflasher.fabricrplog;

import fireflasher.fabricrplog.client.FabricrplogClient;
import fireflasher.fabricrplog.config.json.ServerConfig;
import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static fireflasher.fabricrplog.client.FabricrplogClient.CONFIG;

public class ChatLogger {

    public static Logger LOGGER = Fabricrplog.LOGGER;
    private static String serverIP = "";
    private static String serverName = "Local";
    public static final DateTimeFormatter DATE  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME  = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static File log;
    private static List<String> channellist = new ArrayList<>();
    private static String timedmessage = "";
    private static boolean error;


    public static void servercheck(){
        String address = MinecraftClient.getInstance().getNetworkHandler().getConnection().getAddress().toString();
        String serverNameTemp = address.split("/")[0].split("\\.")[1];
        String ip = address.split("/")[1];
        ip = ip.split(":")[0];

        ServerConfig serverConfig = CONFIG.getServerObject(ip);
        if( serverConfig != null){
            channellist = serverConfig.getServerDetails().getServerKeywords();
            if(!address.split("/")[0].contains(serverName) || serverName.equals("Local")) {
                serverName = getServerNameShortener(serverConfig.getServerDetails().getServerNames());
            }
        }
        else{
            channellist = CONFIG.getKeywords();
            serverName = serverNameTemp;
        }
        serverIP = ip;
    }


    public static void chatFilter(String chat){

        // TODO: Debug
        /*
        for(String debug: channellist){
            LOGGER.info(debug + " chatFilter");
        }
         */
        if( MinecraftClient.getInstance().getNetworkHandler() != null && !MinecraftClient.getInstance().getNetworkHandler().getConnection().isLocal()) servercheck();
        else{
            serverName = "Local";
            channellist = CONFIG.getKeywords();
        }

        boolean isChannel = channellist.stream().anyMatch(chat::contains);
        if (isChannel) addMessage(chat);



    }

    public void setup() {
        String path = FabricrplogClient.getFolder();
        if(!new File(path).exists())new File(path).mkdir();
        log = new File(path + serverName, LocalDateTime.now().format(DATE) + ".txt");

        for(ServerConfig serverConfig: CONFIG.getList()){
            organizeFolders(serverConfig);

            String server_name = getServerNameShortener(serverConfig.getServerDetails().getServerNames());
            String Path = FabricrplogClient.getFolder() + server_name;

            log = new File(Path ,LocalDateTime.now().format(DATE) + ".txt");
            File[] files = new File(Path).listFiles();
            if(files == null){}
            else {
                for (File textfile : files) {
                    if (textfile.toString().endsWith(".txt") && textfile.compareTo(log) != 0 ) {
                        try {
                            String filename  = textfile.toString().replaceFirst("\\.txt", ".zip");

                            FileOutputStream fos = new FileOutputStream(filename);
                            ZipOutputStream zipOut = new ZipOutputStream(fos);

                            File fileToZip = new File(textfile.toString());
                            FileInputStream fis = new FileInputStream(fileToZip);

                            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                            zipOut.putNextEntry(zipEntry);

                            byte[] bytes = new byte[1024];
                            int length;
                            while ((length = fis.read(bytes)) >= 0) {
                                zipOut.write(bytes, 0, length);
                            }
                            zipOut.close();
                            fis.close();
                            fos.close();

                            if(new File(filename).exists()) fileToZip.delete();
                        }
                        catch (IOException e){
                            Text logger_zipwarning  =  Text.translatable("rplog.logger.chatlogger.zip_warning");
                            LOGGER.warn(logger_zipwarning);
                        }
                    }
                }
            }
        }
    }

    private static void addMessage(String chat){
        String Path = FabricrplogClient.getFolder() + serverName;
        if(!log.toString().contains(LocalDateTime.now().format(DATE)) || !log.getPath().equalsIgnoreCase(Path)) {
            LocalDateTime today = LocalDateTime.now();
            String date = today.format(DATE);
            String Filename = date + ".txt";
            log = new File(Path, Filename);
            if(error)log = new File(FabricrplogClient.getFolder(), date + "-error.txt");
            if (!log.exists()) {
                try {
                    File path = new File(Path);
                    path.mkdir();
                    log.createNewFile();
                } catch (IOException e) {
                    Text logger_creationwarning = Text.translatable("rplog.logger.chatlogger.creation_warning");
                    LOGGER.warn(logger_creationwarning + " " + log.toString());
                    error = true;
                }
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(log, true));
            BufferedReader br = new BufferedReader(new FileReader(log));
            LocalDateTime date = LocalDateTime.now();

            String time = "[" + date.format(TIME) + "] ";
            String message = time + chat;

            String collect = br.lines().collect(Collectors.joining(""));
            if(collect.isEmpty()) bw.append(message);
            else if (!timedmessage.equalsIgnoreCase(chat))bw.append("\n" + message);
            bw.close();

            timedmessage = chat;

        } catch (IOException e) {
            Text logger_writewarning = Text.translatable("rplog.logger.chatlogger.write_warning");
            LOGGER.warn( logger_writewarning + " " + log.toString());
        }
    }

    public static String getServerNameShortener(List<String> namelist){
        int[] lenght = new int[2];
        lenght[0] = namelist.get(0).length();
        Pattern serverAddress = Pattern.compile("[A-z]{1,}");
        if(namelist.size() != 1){
            for(String name:namelist){
                if(!serverAddress.matcher(name).find()) continue;

                if(lenght[0] > name.length()){
                    lenght[0] = name.length();
                    lenght[1] = namelist.indexOf(name);
                }
            }
        }
        String name = namelist.get(lenght[1]);
        if(serverAddress.matcher(name).find()){
            Pattern pattern = Pattern.compile("\\.");
            Matcher match = pattern.matcher(name);
            int count = 0;
            while (match.find()) {
                count++;
            }
            if (count > 1) name = name.split("\\.", 2)[1];
            name = name.split("\\.")[0];
        }
        return name;
    }

    private boolean organizeFolders(ServerConfig serverConfig){
        List<String> serverNameList = serverConfig.getServerDetails().getServerNames();
        Pattern serverAddress = Pattern.compile("[A-z]{1,}");
        for(String serverName: serverNameList){
            if(serverAddress.matcher(serverName).find()) continue;

            String path = FabricrplogClient.getFolder() + serverName;
            File ipFolder = new File(path);

            if(!ipFolder.exists())continue;

            File[] ipFolderFiles = ipFolder.listFiles();
            if(ipFolderFiles == null){
                ipFolder.delete();
                continue;
            }

            File newFolder = new File(FabricrplogClient.getFolder() + ChatLogger.getServerNameShortener(serverConfig.getServerDetails().getServerNames()));

            if(newFolder.exists()) {
                if(newFolder.listFiles().length == 0) {
                    newFolder.delete();
                    ipFolder.renameTo(newFolder);
                }else {
                    File[] newFolderFiles = newFolder.listFiles();
                    if (newFolderFiles.length < ipFolderFiles.length){
                        moveFiles(newFolder, ipFolder);
                        ipFolder.renameTo(newFolder);
                    }
                    else moveFiles(ipFolder, newFolder);
                }
            }
            else ipFolder.renameTo(newFolder);


        }
        return true;
    }

    private boolean moveFiles(File sourceFolder, File newFolder) {
        List<Path> folderstodelete = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(sourceFolder.toPath())) {
            pathStream.forEach(path1 -> {
                String filename = path1.getFileName().toString();
                if(filename.equals(sourceFolder.getName()));
                else{
                    try {
                        if (Files.isRegularFile(path1))
                            Files.move(path1, Path.of(newFolder + path1.toString().replace(sourceFolder.toString(),"")));
                        if (Files.isDirectory(path1)) {
                            Files.createDirectory(Path.of(newFolder + path1.toString().replace(sourceFolder.toString(),"")));
                            folderstodelete.add(path1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = folderstodelete.size() - 1; i > -1; i--  ) {
            folderstodelete.get(i).toFile().delete();
        }
        sourceFolder.delete();
        return true;
    }
}