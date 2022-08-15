package me.asakura_kukii.siegefishing.config.io.verifier.basic;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VerifyInteger2ItemStackMap extends Verifier {

    public VerifyInteger2ItemStackMap() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return verifyInteger2ItemStackMap(s, fileName, path, root, obj);
    }

    public static Object verifyInteger2ItemStackMap(String s, String fileName, String path, String root, Object obj) {
        HashMap<Integer, ItemStack> integer2ItemStackMap = new HashMap<>();
        boolean formatCorrect = true;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(s));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            integer2ItemStackMap = (HashMap<Integer, ItemStack>) dataInput.readObject();
            dataInput.close();
        } catch (Exception ignored) {
            FileIO.addErrorMsg(fileName, root, path, "[" + s + "] base 64 error");
            formatCorrect = false;
        }
        if (formatCorrect) {
            return integer2ItemStackMap;
        } else {
            FileIO.putError(fileName);
            return new HashMap<>();
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        HashMap<Integer, ItemStack> integer2ItemStackMap = (HashMap<Integer, ItemStack>) obj;
        String s = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(integer2ItemStackMap);
            dataOutput.close();
            s = Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        fC.set(path, s);
        return fC;
    }
}
