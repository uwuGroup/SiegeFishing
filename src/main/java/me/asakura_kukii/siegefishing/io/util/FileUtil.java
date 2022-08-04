package me.asakura_kukii.siegefishing.io.util;

import me.asakura_kukii.siegefishing.main.Main;
import me.asakura_kukii.siegefishing.utility.format.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static me.asakura_kukii.siegefishing.SiegeFishing.*;

public class FileUtil {
    public FileConfiguration fC;
    public File f;


    public FileUtil(File f2) {
        f = f2;
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fC = YamlConfiguration.loadConfiguration(f);
    }

    public void set(String path, Object val) {
        fC.set(path, val);
        save();
    }

    public void save(){
        try {
            fC.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFileFromResource(String resource, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = pluginInstance.getResource(resource);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while (true) {
                assert is != null;
                if (!((length = is.read(buffer)) > 0)) break;
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
        } catch (Exception ignored) {
        }
    }

    public static File loadSubfolder(String name) {
        if (new File(pluginFolder, name).exists()) {
            File newFolder = new File(pluginFolder, name);
            if (!newFolder.isDirectory()) {
                if(newFolder.delete()) {
                    if(newFolder.mkdir()) {
                        server.getConsoleSender().sendMessage(Color.ANSI_GREEN + consolePluginPrefix + Color.ANSI_WHITE + "Creating directory [" + name + "]");
                        return newFolder;
                    }
                }
            }
            return newFolder;
        } else {
            File newFolder = new File(pluginFolder, name);
            if (newFolder.mkdir()) {
                server.getConsoleSender().sendMessage(Color.ANSI_GREEN + consolePluginPrefix + Color.ANSI_WHITE + "Creating directory [" + name + "]");
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
