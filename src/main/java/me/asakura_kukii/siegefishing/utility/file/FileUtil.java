package me.asakura_kukii.siegefishing.utility.file;

import me.asakura_kukii.siegefishing.main.Main;
import me.asakura_kukii.siegefishing.utility.format.ColorHandler;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.Iterator;
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

    public static void copyJarResourcesRecursively(File to, String from) throws IOException {
        Path toPath = to.toPath();
        final URL configFolderURL = Main.getInstance().getClass().getResource("/" + from);
        if (configFolderURL == null) {
            return;
        }
        JarURLConnection jarConnection = (JarURLConnection) configFolderURL.openConnection();
        JarFile jarFile = jarConnection.getJarFile();
        for (Iterator<JarEntry> it = jarFile.entries().asIterator(); it.hasNext();) {
            JarEntry entry = it.next();
            if (entry.getName().startsWith(jarConnection.getEntryName())) {
                if (!entry.isDirectory()) {
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
    }
}
