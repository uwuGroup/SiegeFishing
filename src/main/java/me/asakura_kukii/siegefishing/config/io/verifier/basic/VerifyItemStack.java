package me.asakura_kukii.siegefishing.config.io.verifier.basic;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.FileIO;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
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

public class VerifyItemStack extends Verifier {

    public VerifyItemStack() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        return (ItemStack) VerifyItemStack.verifyItemStack(s, fileName, path, root, obj);
    }

    public static Object verifyItemStack(String s, String fileName, String path, String root, Object obj) {
        ItemStack iS = new ItemStack(Material.AIR);
        boolean formatCorrect = true;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(s));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            iS = (ItemStack) dataInput.readObject();
            dataInput.close();
        } catch (Exception ignored) {
            FileIO.addErrorMsg(fileName, root, path, "[" + s + "] base 64 error");
            formatCorrect = false;
        }
        if (formatCorrect) {
            return iS;
        } else {
            FileIO.putError(fileName);
            return obj;
        }
    }

    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        ItemStack iS = (ItemStack) obj;
        String s = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(iS);
            dataOutput.close();
            s = Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        fC.set(path, s);
        return fC;
    }
}
