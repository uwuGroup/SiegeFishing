package me.asakura_kukii.siegefishing.loader.method.common;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodNodeData;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodType;
import me.asakura_kukii.siegefishing.loader.common.FileIO;
import me.asakura_kukii.siegefishing.loader.common.FormatHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public abstract class MethodIO {
    public static MethodNodeData loadTree(FileConfiguration fC, String fileName) {
        if (fC.contains("methodTree")) {
            ConfigurationSection cS = fC.getConfigurationSection("methodTree");
            MethodNodeData mND = new MethodNodeData();
            assert cS != null;
            mND = loadNode(cS, mND, fileName);
            return mND;
        } else {
            FileIO.fileStatusMapper.put(fileName, false);
            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + "methodTree is empty\n");
        }
        return null;
    }

    public static MethodNodeData loadNode(ConfigurationSection cS, MethodNodeData mND, String fileName) {
        MethodType mT = MethodType.DAMAGELINE;
        mT = (MethodType) FormatHandler.checkConfigurationFormat(cS, fileName, "type", mT, true);

        List<Object> dataList = new ArrayList<>();
        if (mT.mIO != null) {
            dataList = mT.mIO.loadData(cS, fileName, mT);
        }


        mND.mT = mT;
        mND.data = dataList;

        SiegeFishing.server.getConsoleSender().sendMessage(mT + dataList.toString() + mND.trigger);


        if (cS.contains("child") && cS.getConfigurationSection("child") != null) {
            ConfigurationSection childCS = cS.getConfigurationSection("child");
            assert childCS != null;
            SiegeFishing.server.getConsoleSender().sendMessage(childCS.getKeys(false).toString());
            for (String s: childCS.getKeys(false)) {
                String currentPath = childCS.getCurrentPath() + ".";
                if (currentPath.matches(".")) {
                    currentPath = "";
                }
                if (!mND.mT.triggerList.contains(s)) {
                    FileIO.fileStatusMapper.put(fileName, false);
                    FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + s + " is not valid trigger\n");
                } else {
                    MethodNodeData childMND = new MethodNodeData();
                    childMND.trigger = s;
                    ConfigurationSection nextNodeCS = childCS.getConfigurationSection(s);
                    childMND = loadNode(nextNodeCS, childMND, fileName);
                    if (childMND != null) {
                        mND.next.add(childMND);
                    }
                }
            }
        }

        return mND;
    }

    public abstract List<Object> loadData(ConfigurationSection cS, String fileName, MethodType mT);
}
