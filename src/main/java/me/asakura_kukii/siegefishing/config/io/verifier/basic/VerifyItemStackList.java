package me.asakura_kukii.siegefishing.config.io.verifier.basic;

import me.asakura_kukii.siegefishing.config.data.FileData;
import me.asakura_kukii.siegefishing.config.io.verifier.Verifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VerifyItemStackList extends Verifier {

    public VerifyItemStackList() {}

    @Override
    public Object get(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<ItemStack> iSL = new ArrayList<>();
        for (String s : sL) {
            ItemStack iS = (ItemStack) VerifyItemStack.verifyItemStack(s, fileName, path, root, obj);
            iSL.add(iS);
        }
        return iSL;
    }


    @Override
    public FileConfiguration set(FileConfiguration fC, FileData fD, String path, Object obj) {
        List<String> sL = new ArrayList<>();
        List<ItemStack> iSL = (List<ItemStack>) obj;
        for (ItemStack iS : iSL) {
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
            sL.add(s);
        }
        fC.set(path, sL);
        return fC;
    }
}
