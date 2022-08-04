package me.asakura_kukii.siegefishing.io.verifier.basic;

import me.asakura_kukii.siegefishing.io.verifier.common.Verifier;
import me.asakura_kukii.siegefishing.utility.format.Color;
import me.asakura_kukii.siegefishing.utility.format.UnicodeHandler;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class VerifyColoredString extends Verifier {

    public VerifyColoredString() {
    }

    @Override
    public Object verify(ConfigurationSection cS, String fileName, String path, String root, Object obj, File folder) {
        String s = cS.getString(path);
        s = UnicodeHandler.map(s);
        s = Color.mapColorCode(s);
        assert s != null;
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
