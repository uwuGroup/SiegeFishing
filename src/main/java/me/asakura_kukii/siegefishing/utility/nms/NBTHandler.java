package me.asakura_kukii.siegefishing.utility.nms;

import me.asakura_kukii.siegefishing.SiegeFishing;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class NBTHandler {
    public static boolean hasSiegeWeaponCompoundTag(ItemStack iS) {
        if (iS != null && SiegeFishing.pluginName != null && iS.getItemMeta() != null && iS.getItemMeta().hasCustomModelData()) {
            ItemStack clonedIS = iS.clone();
            net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(clonedIS);
            if(nmsItemStack.hasTag()) {
                CompoundTag NBTCompound = nmsItemStack.getTag();
                assert NBTCompound != null;
                if(NBTCompound.contains(SiegeFishing.pluginName)) {
                    CompoundTag cT = (CompoundTag) NBTCompound.get(SiegeFishing.pluginName);
                    assert cT != null;
                    return true;
                }
            }
        }
        return false;
    }

    public static Boolean contains(ItemStack iS, String s) {
        if (iS != null && SiegeFishing.pluginName != null) {
            ItemStack clonedIS = iS.clone();
            net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(clonedIS);
            if(nmsItemStack.hasTag()) {
                CompoundTag NBTCompound = nmsItemStack.getTag();

                assert NBTCompound != null;
                if(NBTCompound.contains(SiegeFishing.pluginName)) {
                    CompoundTag cT = (CompoundTag) NBTCompound.get(SiegeFishing.pluginName);
                    assert cT != null;
                    return cT.contains(s);
                }
            }
        }
        return false;
    }

    public static ItemStack remove(ItemStack iS, String s) {
        if (iS != null && SiegeFishing.pluginName != null) {
            ItemStack clonedIS = iS.clone();
            net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(clonedIS);
            if(nmsItemStack.hasTag()) {
                CompoundTag NBTCompound = nmsItemStack.getTag();

                assert NBTCompound != null;
                if(NBTCompound.contains(SiegeFishing.pluginName)) {
                    CompoundTag cT = (CompoundTag) NBTCompound.get(SiegeFishing.pluginName);
                    assert cT != null;
                    if (cT.contains(s)) {
                        cT.remove(s);
                    }
                    NBTCompound.remove(SiegeFishing.pluginName);
                    NBTCompound.put(SiegeFishing.pluginName, cT);
                }
                nmsItemStack.setTag(NBTCompound);
                return CraftItemStack.asBukkitCopy(nmsItemStack);
            }
        }
        return null;
    }

    public static Object get(String nbtString, String s, Class<?> c) {
        try {
            CompoundTag cT = TagParser.parseTag(nbtString);
            return get(cT, s, c);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static Object get(CompoundTag cT, String s, Class<?> c) {
        if (c == null) {
            if (s.matches("")) {
                return cT.toString();
            }
        }
        if (cT.contains(s)) {
            if (c == null) {
                return Objects.requireNonNull(cT.get(s)).toString();
            }
            if (c.getSimpleName().matches("Integer")) {
                return cT.getInt(s);
            }
            if (c.getSimpleName().matches("String")) {
                return cT.getString(s);
            }
            if (c.getSimpleName().matches("Double")) {
                return cT.getDouble(s);
            }
            if (c.getSimpleName().matches("Boolean")) {
                return cT.getBoolean(s);
            }
            return cT.get(s);
        }
        return null;
    }

    public static Object get(ItemStack iS, String s, Class<?> c) {
        if (iS != null && SiegeFishing.pluginName != null) {
            ItemStack clonedIS = iS.clone();
            net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(clonedIS);
            if(nmsItemStack.hasTag()) {
                CompoundTag NBTCompound = nmsItemStack.getTag();

                assert NBTCompound != null;
                if(NBTCompound.contains(SiegeFishing.pluginName)) {
                    CompoundTag cT = (CompoundTag) NBTCompound.get(SiegeFishing.pluginName);
                    assert cT != null;
                    return get(cT, s, c);
                }
            }
        }
        return null;
    }

    public static CompoundTag set(CompoundTag cT, String s, Object o, Boolean b) {
        if (cT.contains(s)) {
            cT.remove(s);
        }
        if (o instanceof Integer) {
            cT.putInt(s, (int) o);
        } else if (o instanceof String) {
            if (b) {
                try {
                    if (s.matches("")) {
                        cT = TagParser.parseTag((String) o);
                    } else {
                        cT.put(s, TagParser.parseTag((String) o));
                    }
                } catch (Exception ignored) {
                }
            } else {
                cT.putString(s, (String) o);
            }
        } else if (o instanceof Double) {
            cT.putDouble(s, (Double) o);
        } else if (o instanceof Boolean) {
            cT.putBoolean(s, (Boolean) o);
        }
        return cT;
    }

    public static String set(String nbtString, String s, Object o, Boolean b) {
        try {
            CompoundTag cT;
            if (nbtString.matches("")) {
                cT = new CompoundTag();
            } else {
                cT = TagParser.parseTag(nbtString);
            }

            cT = set(cT, s, o, b);
            return cT.toString();
        } catch (Exception ignored) {
        }
        return null;
    }

    public static ItemStack set(ItemStack iS, String s, Object o, Boolean b) {
        if (iS != null && SiegeFishing.pluginName != null) {
            ItemStack clonedIS = iS.clone();
            net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(clonedIS);
            CompoundTag NBTCompound = (nmsItemStack.hasTag()) ? nmsItemStack.getTag() : new CompoundTag();
            CompoundTag cT;
            assert NBTCompound != null;
            if(!NBTCompound.contains(SiegeFishing.pluginName)) {
                cT = new CompoundTag();
                cT = set(cT, s, o, b);
                NBTCompound.put(SiegeFishing.pluginName, cT);
            } else {
                cT = (CompoundTag) NBTCompound.get(SiegeFishing.pluginName);
                assert cT != null;
                cT = set(cT, s, o, b);
                NBTCompound.remove(SiegeFishing.pluginName);
                NBTCompound.put(SiegeFishing.pluginName, cT);
            }
            nmsItemStack.setTag(NBTCompound);
            return CraftItemStack.asBukkitCopy(nmsItemStack);
        }
        return null;
    }
}
