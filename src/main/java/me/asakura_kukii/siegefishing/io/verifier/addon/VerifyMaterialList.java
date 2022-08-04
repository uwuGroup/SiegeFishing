package me.asakura_kukii.siegefishing.io.verifier.addon;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyMaterialList extends Verifier {
    public VerifyMaterialList() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        List<String> sL = cS.getStringList(path);

        List<Material> mL = new ArrayList<>();
        for (String s : sL) {
            Material m = (Material) VerifyMaterial.verifyMaterial(s, fileName, path, root, obj);
            mL.add(m);
        }
        return mL;
    }
}
