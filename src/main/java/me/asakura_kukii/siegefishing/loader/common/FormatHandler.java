package me.asakura_kukii.siegefishing.loader.common;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.item.gun.reload.ReloadType;
import me.asakura_kukii.siegefishing.handler.item.hand.HandData;
import me.asakura_kukii.siegefishing.handler.nonitem.method.common.MethodType;
import me.asakura_kukii.siegefishing.handler.nonitem.particle.ParticleData;
import me.asakura_kukii.siegefishing.handler.nonitem.sound.SoundData;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class FormatHandler {
    public static boolean checkVectorFormat(String s) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==3 || s.split("\\^").length==5) {
                for (String s2 : s.split("\\^")) {
                    try {
                        Double.parseDouble(s2);
                    } catch (Exception ignored) {
                        formatCorrect = false;
                    }
                }
                return formatCorrect;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean checkBreakableBlockFormat(String s) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==2) {
                int index = 0;
                for (String s2 : s.split("\\^")) {
                    try {
                        if (index == 0) {
                            if (Material.matchMaterial(s2) == null) {
                                formatCorrect = false;
                            }
                        } else {
                            Double.parseDouble(s2);
                        }
                        index++;
                    } catch (Exception ignored) {
                        formatCorrect = false;
                    }
                }
                return formatCorrect;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean checkReplaceableBlockFormat(String s) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==2) {
                for (String s2 : s.split("\\^")) {
                    try {
                        if (Material.matchMaterial(s2) == null) {
                            formatCorrect = false;
                        }
                    } catch (Exception ignored) {
                        formatCorrect = false;
                    }
                }
                return formatCorrect;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean checkGunModificationFormat(String s) {
        boolean formatCorrect = true;
        if (s.contains("^")) {
            if (s.split("\\^").length==4) {
                int index = 0;
                for (String s2 : s.split("\\^")) {
                    try {
                        if (index == 0) {
                            if (!FileType.GUN.map.containsKey(s2)) {
                                formatCorrect = false;
                            }
                        } else {
                            Integer.parseInt(s2);
                        }
                        index++;
                    } catch (Exception ignored) {
                        formatCorrect = false;
                    }
                }
                return formatCorrect;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean checkIntFormat(String s) {
        boolean formatCorrect = true;
        try {
            Integer.parseInt(s);
        } catch (Exception ignored) {
            formatCorrect = false;
        }
        return formatCorrect;
    }

    public static boolean checkDoubleFormat(String s) {
        boolean formatCorrect = true;
        try {
            Double.parseDouble(s);
        } catch (Exception ignored) {
            formatCorrect = false;
        }
        return formatCorrect;
    }

    public static <T> T cast(Object o, Class<T> type) {
        if (type.isInstance(o)) return type.cast(o);
        return null;
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    @NonNull
    public static Object checkConfigurationFormat(ConfigurationSection cS, String fileName, String path, Object obj, boolean notEmpty) {
        String currentPath = cS.getCurrentPath() + ".";
        if (currentPath.matches(".")) {
            currentPath = "";
        }

        if (cS.contains(path)) {
            String s = cS.getString(path);
            assert s != null;
            if (s.matches("")) {
                if (notEmpty) {
                    FileIO.fileStatusMapper.put(fileName, false);
                    FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + path + " is empty\n");
                } else {
                    FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + path + " is empty, neglected\n");
                }
                return obj;
            } else {
                if (obj instanceof String) {
                    return s;
                }
                if (obj instanceof Integer) {
                    try {
                        return Integer.parseInt(s);
                    } catch(Exception ignored) {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Integer\n");
                        return obj;
                    }
                }
                if (obj instanceof Float) {
                    try {
                        return Float.parseFloat(s);
                    } catch(Exception ignored) {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Float\n");
                        return obj;
                    }
                }
                if (obj instanceof Double) {
                    try {
                        return Double.parseDouble(s);
                    } catch(Exception ignored) {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Double\n");
                        return obj;
                    }
                }
                if (obj instanceof Boolean) {
                    if (s.matches("true") || s.matches("false")) {
                        return Boolean.parseBoolean(s);
                    } else {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Boolean\n");
                        return obj;
                    }
                }
                if (obj instanceof Vector3D) {
                    if (checkVectorFormat(s)) {
                        return new Vector3D(Double.parseDouble(s.split("\\^")[0]), Double.parseDouble(s.split("\\^")[1]), Double.parseDouble(s.split("\\^")[2]));
                    } else {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Vector3D\n");
                        return obj;
                    }
                }
                if (obj instanceof ArrayList) {
                    return cS.getStringList(path);
                }
                if (obj instanceof Material) {
                    if (Material.matchMaterial(s) != null) {
                        return Objects.requireNonNull(Material.matchMaterial(s));
                    } else {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Material\n");
                        return obj;
                    }
                }
                if (obj instanceof Particle) {
                    try {
                        return Particle.valueOf(s);
                    } catch (Exception ignored) {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Particle\n");
                        return obj;
                    }
                }
                if (obj instanceof Color) {
                    if (s.matches("white")) {
                        return Color.WHITE;
                    } else if (s.matches("silver")) {
                        return Color.SILVER;
                    } else if (s.matches("gray")) {
                        return Color.GRAY;
                    } else if (s.matches("black")) {
                        return Color.BLACK;
                    } else if (s.matches("red")) {
                        return Color.RED;
                    } else if (s.matches("maroon")) {
                        return Color.MAROON;
                    } else if (s.matches("yellow")) {
                        return Color.YELLOW;
                    } else if (s.matches("olive")) {
                        return Color.OLIVE;
                    } else if (s.matches("lime")) {
                        return Color.LIME;
                    } else if (s.matches("green")) {
                        return Color.GREEN;
                    } else if (s.matches("aqua")) {
                        return Color.AQUA;
                    } else if (s.matches("teal")) {
                        return Color.TEAL;
                    } else if (s.matches("blue")) {
                        return Color.BLUE;
                    } else if (s.matches("navy")) {
                        return Color.NAVY;
                    } else if (s.matches("fuchsia")) {
                        return Color.FUCHSIA;
                    } else if (s.matches("purple")) {
                        return Color.PURPLE;
                    } else if (s.matches("orange")) {
                        return Color.ORANGE;
                    } else {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Color\n");
                        return obj;
                    }
                }
                if (obj instanceof Enchantment) {
                    if (Enchantment.getByKey(NamespacedKey.minecraft(path)) != null) {
                        try {
                            Integer.parseInt(s);
                            return Objects.requireNonNull(Enchantment.getByKey(NamespacedKey.minecraft(path)));
                        } catch(Exception ignored) {
                            FileIO.fileStatusMapper.put(fileName, false);
                            FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Enchantment\n");
                            return obj;
                        }
                    } else {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not Enchantment\n");
                        return obj;
                    }
                }
                if (obj instanceof HandData) {
                    if (FileType.HAND.map.containsKey(s)) {
                        return FileType.HAND.map.get(s);
                    } else {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not hand\n");
                        return obj;
                    }
                }
                if (obj instanceof ReloadType) {
                    try {
                        return ReloadType.valueOf(s.toUpperCase(Locale.ROOT));
                    } catch (Exception ignored) {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not ReloadType\n");
                        return obj;
                    }
                }
                if (obj instanceof MethodType) {
                    try {
                        return MethodType.valueOf(s.toUpperCase(Locale.ROOT));
                    } catch (Exception ignored) {
                        FileIO.fileStatusMapper.put(fileName, false);
                        FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " is not MethodType\n");
                        return obj;
                    }
                }
            }
        } else {
            if (notEmpty) {
                FileIO.fileStatusMapper.put(fileName, false);
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " does not exist\n");
            } else {
                FileIO.fileMessageMapper.put(fileName, FileIO.fileMessageMapper.get(fileName) + SiegeFishing.consolePluginPrefix + currentPath + path + " does not exist, neglected\n");
            }
        }
        return obj;
    }


    public static List<Material> materialListCreator(ConfigurationSection cS, String fN, String path, List<String> sL) {
        String currentPath = cS.getCurrentPath() + ".";
        if (currentPath.matches(".")) {
            currentPath = "";
        }
        List<Material> mL = new ArrayList<>();
        for (String s : sL) {
            if (Material.matchMaterial(s) != null) {
                mL.add(Material.matchMaterial(s));
            } else {
                FileIO.fileStatusMapper.put(fN, false);
                FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + currentPath + path + "-" + s + " is not Material\n");
            }
        }
        return mL;
    }

    public static List<Vector3D> vector3DListCreator(ConfigurationSection cS, String fN, String path, List<String> sL) {
        String currentPath = cS.getCurrentPath() + ".";
        if (currentPath.matches(".")) {
            currentPath = "";
        }
        List<Vector3D> vL = new ArrayList<>();
        for (String s : sL) {
            if (checkVectorFormat(s)) {
                vL.add(Vector3D.string2Vector(s));
            } else {
                FileIO.fileStatusMapper.put(fN, false);
                FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + currentPath + path + "-" + s + " is not Vector\n");
            }
        }
        return vL;
    }
    public static List<ParticleData> particleDataListCreator(ConfigurationSection cS, String fN, String path, List<String> sL) {
        String currentPath = cS.getCurrentPath() + ".";
        if (currentPath.matches(".")) {
            currentPath = "";
        }
        List<ParticleData> pDL = new ArrayList<>();
        for (String s : sL) {
            if (FileType.PARTICLE.map.containsKey(s)) {
                pDL.add((ParticleData) FileType.PARTICLE.map.get(s));
            } else {
                FileIO.fileStatusMapper.put(fN, false);
                FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + currentPath + path + "-" + s + " is not ParticleData\n");
            }
        }
        return pDL;
    }
    public static List<SoundData> soundDataListCreator(ConfigurationSection cS, String fN, String path, List<String> sL) {
        String currentPath = cS.getCurrentPath() + ".";
        if (currentPath.matches(".")) {
            currentPath = "";
        }
        List<SoundData> sDL = new ArrayList<>();
        for (String s : sL) {
            if (FileType.SOUND.map.containsKey(s)) {
                sDL.add((SoundData) FileType.SOUND.map.get(s));
            } else {
                FileIO.fileStatusMapper.put(fN, false);
                FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + currentPath + path + "-" + s + " is not SoundData\n");
            }
        }
        return sDL;
    }
    public static HashMap<Material, Double> breakableBlockMapCreator(ConfigurationSection cS, String fN, String path, List<String> sL) {
        String currentPath = cS.getCurrentPath() + ".";
        if (currentPath.matches(".")) {
            currentPath = "";
        }
        HashMap<Material, Double> bBM = new HashMap<>();
        for (String s : sL) {
            if (checkBreakableBlockFormat(s)) {
                int index = 0;
                Material m = Material.ANDESITE;
                double health = 0.00;
                for (String s2 : s.split("\\^")) {
                    if (index == 0) {
                        m = Material.matchMaterial(s2);
                    } else {
                        health = Double.parseDouble(s2);
                    }
                    index++;
                }
                bBM.put(m, health);
            } else {
                FileIO.fileStatusMapper.put(fN, false);
                FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + currentPath + path + "-" + s + " is not block map\n");
            }
        }
        return bBM;
    }
    public static HashMap<Material, Material> replaceableBlockMapCreator(ConfigurationSection cS, String fN, String path, List<String> sL) {
        String currentPath = cS.getCurrentPath() + ".";
        if (currentPath.matches(".")) {
            currentPath = "";
        }
        HashMap<Material, Material> rBM = new HashMap<>();
        for (String s : sL) {
            if (checkReplaceableBlockFormat(s)) {
                int index = 0;
                Material m1 = Material.ANDESITE;
                Material m2 = Material.ANDESITE;
                for (String s2 : s.split("\\^")) {
                    if (index == 0) {
                        m1 = Material.matchMaterial(s2);
                    } else {
                        m2 = Material.matchMaterial(s2);
                    }
                    index++;
                }
                rBM.put(m1, m2);
            } else {
                FileIO.fileStatusMapper.put(fN, false);
                FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + currentPath + path + "-" + s + " is not replaceable block map\n");
            }
        }
        return rBM;
    }
    public static HashMap<String, Integer> gunModificationListCreator(ConfigurationSection cS, String fN, String path, List<String> sL) {
        String currentPath = cS.getCurrentPath() + ".";
        if (currentPath.matches(".")) {
            currentPath = "";
        }
        HashMap<String, Integer> gMM = new HashMap<>();
        for (String s : sL) {
            if (checkGunModificationFormat(s)) {
                gMM.put(s.split("\\^")[0], Integer.parseInt(s.split("\\^")[1]) * 100 + Integer.parseInt(s.split("\\^")[2]) * 10 + Integer.parseInt(s.split("\\^")[3]));
            } else {
                FileIO.fileStatusMapper.put(fN, false);
                FileIO.fileMessageMapper.put(fN, FileIO.fileMessageMapper.get(fN) + SiegeFishing.consolePluginPrefix + currentPath + path + "-" + s + " is not Gun Modification\n");
            }
        }
        return gMM;
    }

    public static List<String> coloredStringListCreator(List<String> sL) {
        List<String> cSL = new ArrayList<>();
        for (String s : sL) {
            cSL.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return cSL;
    }
}
