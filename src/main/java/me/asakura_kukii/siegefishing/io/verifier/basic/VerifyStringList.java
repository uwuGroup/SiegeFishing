package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class VerifyStringList extends Verifier {

    public VerifyStringList() {}

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        return cS.getStringList(path);
    }
}
