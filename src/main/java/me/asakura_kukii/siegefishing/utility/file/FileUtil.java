package me.asakura_kukii.siegefishing.utility.file;

import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import me.asakura_kukii.siegefishing.config.io.verifier.VerifierType;
import me.asakura_kukii.siegefishing.main.Main;
import me.asakura_kukii.siegefishing.utility.format.ColorHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static me.asakura_kukii.siegefishing.SiegeFishing.*;

public class FileUtil {
    public static File loadSubfolder(String name) {
        if (new File(pluginFolder, name).exists()) {
            File newFolder = new File(pluginFolder, name);
            if (!newFolder.isDirectory()) {
                if(newFolder.delete()) {
                    if(newFolder.mkdir()) {
                        server.getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + consolePluginPrefix + ColorHandler.ANSI_WHITE + "Creating directory [" + name + "]");
                        return newFolder;
                    }
                }
            }
            return newFolder;
        } else {
            File newFolder = new File(pluginFolder, name);
            if (newFolder.mkdir()) {
                server.getConsoleSender().sendMessage(ColorHandler.ANSI_GREEN + consolePluginPrefix + ColorHandler.ANSI_WHITE + "Creating directory [" + name + "]");
                return newFolder;
            }
            return null;
        }
    }

    public static List<String> checkJarResources(File to, String from) throws IOException {
        List<String> defaultIdentifierList = new ArrayList<>();

        Path toPath = to.toPath();
        final URL configFolderURL = Main.getInstance().getClass().getResource("/" + from);
        if (configFolderURL == null) {
            return new ArrayList<>();
        }
        JarURLConnection jarConnection = (JarURLConnection) configFolderURL.openConnection();
        JarFile jarFile = jarConnection.getJarFile();
        for (Iterator<JarEntry> it = jarFile.entries().asIterator(); it.hasNext();) {
            JarEntry entry = it.next();
            if (entry.getName().startsWith(jarConnection.getEntryName())) {
                if (!entry.isDirectory()) {
                    try (InputStream entryInputStream = jarFile.getInputStream(entry)) {
                        String shortName = entry.getName().split("/")[entry.getName().split("/").length - 1];
                        File tempFile = Paths.get(Main.tempFolder.toPath().toString(), shortName).toFile();
                        Files.copy(entryInputStream, Paths.get(Main.tempFolder.toPath().toString(), shortName));
                        YamlConfiguration yC = YamlConfiguration.loadConfiguration(tempFile);
                        String identifier = "";
                        identifier = (String) Verifier.getObject(yC, from.replace("/", ".") + "." + shortName, "identifier", identifier, VerifierType.STRING, true, null);
                        defaultIdentifierList.add(identifier);
                        tempFile.delete();
                    }
                    try (InputStream entryInputStream = jarFile.getInputStream(entry)) {
                        File f = Paths.get(toPath.toString(), entry.getName().replace(from + "/", "")).toFile();
                        if (!f.exists()) {
                            Files.copy(entryInputStream, Paths.get(toPath.toString(), entry.getName().replace(from + "/", "")));
                        }
                    }
                } else {
                    Files.createDirectories(Paths.get(toPath.toString(), entry.getName().replace(from + "/", "")));
                }
            }
        }
        return defaultIdentifierList;
    }
}
