package me.asakura_kukii.siegefishing.handler.nonitem.potential;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.item.gun.GunData;
import me.asakura_kukii.siegefishing.handler.item.mod.ModHandler;
import me.asakura_kukii.siegefishing.loader.common.FileType;
import me.asakura_kukii.siegefishing.utility.colorcode.ColorCode;
import me.asakura_kukii.siegefishing.utility.coodinate.Vector3D;
import me.asakura_kukii.siegefishing.utility.format.UnicodeHandler;
import me.asakura_kukii.siegefishing.utility.nms.NBTHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PotentialHandler {

    public static Vector3D generate = new Vector3D(255,255,255);
    public static Vector3D update = new Vector3D(224,103,65);
    public static Vector3D normal = new Vector3D(155,155,155);
    public static Vector3D generateVision = new Vector3D(255,187,0);
    public static Vector3D updateVision = new Vector3D(255,187,0);
    public static Vector3D normalVision = new Vector3D(155,155,155);


    //remember to config the spawn percent of visionslot
    public static String colorPotential(String s, String potential, int mode) {

        List<Double> dataList = interpretPotential(potential);
        double generatePercent = dataList.get(1);
        double updatePercent = dataList.get(2);
        double normalPercent = 1 - generatePercent - updatePercent;
        double visionSlotPercent = dataList.get(4);
        String code = "";
        if (mode == 0) {
            int r = (int) Math.floor(generateVision.x * visionSlotPercent + normal.x * (1 - visionSlotPercent));
            int g = (int) Math.floor(generateVision.y * visionSlotPercent + normal.y * (1 - visionSlotPercent));
            int b = (int) Math.floor(generateVision.z * visionSlotPercent + normal.z * (1 - visionSlotPercent));
            code = ColorCode.generateColorCode(r,g,b) + code;
        } else if (mode == 1) {
            int r = (int) Math.floor(generate.x * generatePercent + update.x * updatePercent + normal.x * normalPercent);
            int g = (int) Math.floor(generate.y * generatePercent + update.y * updatePercent + normal.y * normalPercent);
            int b = (int) Math.floor(generate.z * generatePercent + update.z * updatePercent + normal.z * normalPercent);
            code = ColorCode.generateColorCode(r,g,b) + code;
        }
        return code + s;
    }

    public static ItemStack updatePotential(ItemStack iS, PotentialType pT, int index) {
        String s = readPotentialFromNBT(iS, pT, index);
        char[] charList = s.toCharArray();
        StringBuilder sB = new StringBuilder();
        boolean updateComplete = false;
        for (char c : charList) {
            if (c == '_') {
                if (!updateComplete) {
                    sB.append("+");
                    updateComplete = true;
                } else {
                    sB.append("_");
                }

            }
            if (c == '=') {
                sB.append("=");
            }
            if (c == '+') {
                sB.append("+");
            }
            if (c == '*') {
                if (!updateComplete) {
                    sB.append("%");
                    updateComplete = true;
                } else {
                    sB.append("*");
                }
            }
            if (c == '#') {
                sB.append("#");
            }
            if (c == '%') {
                sB.append("%");
            }
        }
        iS = NBTHandler.remove(iS, pT.identifier + index + "");
        iS = NBTHandler.set(iS, pT.identifier + index + "", sB.toString(), false);
        iS = ModHandler.calculateCurrentData(GunData.getData(iS), iS);
        return iS;
    }


    public static List<String> formatPotentialRelatedStringList(ItemStack iS, List<String> sL, PotentialType pT) {

        List<String> potentialString = PotentialHandler.readPotentialFromNBT(iS, pT);
        List<List<Double>> potentialDataList = new ArrayList<>();
        for (String s : potentialString) {
            potentialDataList.add(PotentialHandler.interpretPotential(s));
        }

        List<String> formatStringList = new ArrayList<>();

        for (String s : sL) {
            if(s.contains("@")) {
                String[] stringSplit = s.split("@");
                for(String s2 : stringSplit) {
                    if(s2.contains("#")) {
                        String[] s2SplitList = s2.split("#");
                        String s2Split = s2SplitList[0];
                        if (s2Split.contains("^") && s2Split.split("\\^").length == 2) {
                            try {
                                int index = Integer.parseInt(s2Split.split("\\^")[1]);

                                String key = s2Split.split("\\^")[0];

                                switch (key) {
                                    case "pb":
                                        s = s.replace("@" + s2Split + "#", formPotentialBar(potentialString.get(index)));
                                        break;
                                    case "ppm":
                                        s = s.replace("@" + s2Split + "#", UnicodeHandler.numberStringToUnicode(((int)Math.floor(potentialDataList.get(index).get(0)*100)) + "", 3));
                                        break;
                                    case "ppg":
                                        s = s.replace("@" + s2Split + "#", UnicodeHandler.numberStringToUnicode(((int)Math.floor(potentialDataList.get(index).get(1)*100)) + "", 3));
                                        break;
                                    case "ppu":
                                        s = s.replace("@" + s2Split + "#", UnicodeHandler.numberStringToUnicode(((int)Math.floor(potentialDataList.get(index).get(2)*100)) + "", 3));
                                        break;
                                    case "ppv":
                                        s = s.replace("@" + s2Split + "#", colorPotential(UnicodeHandler.numberStringToUnicode(((int)Math.floor(potentialDataList.get(index).get(3)*100)) + "", 3), potentialString.get(index), 0));
                                        break;
                                    case "ppgu":
                                        s = s.replace("@" + s2Split + "#", colorPotential(UnicodeHandler.numberStringToUnicode(((int)Math.floor(potentialDataList.get(index).get(1)*100)+(int)Math.floor(potentialDataList.get(index).get(2)*100)) + "", 3), potentialString.get(index), 1));
                                        break;
                                }
                            } catch (Exception ignored) {

                            }
                        } else {
                            if (FileType.UNICODE.subMap.containsKey(s2Split)) {
                                s = s.replace("@" + s2Split + "#", (String) FileType.UNICODE.subMap.get(s2Split));
                            }
                        }
                    }
                }
            }

            formatStringList.add(s);
        }

        return formatStringList;
    }

    public static String formPotentialBar (String s) {
        char[] charList = s.toCharArray();
        StringBuilder sB = new StringBuilder();
        for (char c : charList) {
            if (c == '_') {
                sB.append(UnicodeHandler.map(SiegeFishing.potentialNormal));
            }
            if (c == '=') {
                sB.append(UnicodeHandler.map(SiegeFishing.potentialGenerate));
            }
            if (c == '+') {
                sB.append(UnicodeHandler.map(SiegeFishing.potentialUpdate));
            }
            if (c == '*') {
                sB.append(UnicodeHandler.map(SiegeFishing.potentialVisionNormal));
            }
            if (c == '#') {
                sB.append(UnicodeHandler.map(SiegeFishing.potentialVisionGenerate));
            }
            if (c == '%') {
                sB.append(UnicodeHandler.map(SiegeFishing.potentialVisionUpdate));
            }
        }

        return sB.toString();
    }

    public static ItemStack savePotentialToNBT (ItemStack iS, List<String> potentialString, PotentialType pT) {
        for (int i = 0; i < pT.row; i++) {
            iS = NBTHandler.remove(iS, pT.identifier + i + "");
            iS = NBTHandler.set(iS, pT.identifier + i + "", potentialString.get(i), false);
        }
        return iS;
    }

    public static List<String> readPotentialFromNBT (ItemStack iS, PotentialType pT) {
        List<String> potentialString = new ArrayList<>();
        for (int i = 0; i < pT.row; i++) {
            potentialString.add((String) NBTHandler.get(iS, pT.identifier + i + "", String.class));
        }
        return potentialString;
    }

    public static String readPotentialFromNBT (ItemStack iS, PotentialType pT, int index) {
        return (String) NBTHandler.get(iS, pT.identifier + index + "", String.class);
    }

    public static List<Double> interpretPotential(String s) {

        char[] charList = s.toCharArray();
        int generate = 0;
        int update = 0;
        int visionGenerate = 0;
        int visionUpdate = 0;

        for (char c : charList) {
            if (c == '=') {
                generate++;
                continue;
            }
            if (c == '+') {
                update++;
                continue;
            }
            if (c == '#') {
                visionGenerate++;
                continue;
            }
            if (c == '%') {
                visionUpdate++;
                continue;
            }
        }

        int length = s.toCharArray().length;

        double step = 1.00 / (double) length;
        double visionStep = 0.50 / (double) length;
        double generatePercent = (generate + visionGenerate) * step;
        double updatePercent = (update + visionUpdate) * step;
        double visionPercent = (visionGenerate + visionUpdate) * visionStep;
        double totalPercent = generatePercent + updatePercent + visionPercent;
        double visionSlotPercent = ((double)visionGenerate + (double)visionUpdate) / (double)length;

        List<Double> percentList = new ArrayList<>();
        percentList.add(totalPercent);
        percentList.add(generatePercent);
        percentList.add(updatePercent);
        percentList.add(visionPercent);
        percentList.add(visionSlotPercent);
        return percentList;
    }

    public static List<String> generatePotential(int level, PotentialType pT) {
        return generatePotential(level, pT.row, pT.column, pT.visionPercent, pT.minFillPercent, pT.maxFillPercent, pT.lAsMainPotential);
    }


    public static List<String> generatePotential(int level, int row, int column, double visionPercent, double minFillPercent, double maxFillPercent, boolean lAsMainPotential) {
        //l is 1 - 30


        Random r = new Random();
        int distributeRowCount = row;
        if (lAsMainPotential) {
            distributeRowCount = row -1;
        }

        List<Integer> distributedPotentialList = new ArrayList<>();

        int totalCount = row * column;
        int distributedCount = 0;
        if (distributeRowCount != 0) {
            int distributeTotalCount = distributeRowCount * column;
            double distributeStep = (maxFillPercent - minFillPercent) * (double) distributeTotalCount / (column - 1);

            double distributeCount = minFillPercent * (double) distributeTotalCount + (level - 1) * distributeStep - distributeRowCount;

            distributedCount = (int) Math.ceil(distributeCount);
            if (distributedCount > distributeTotalCount) {
                distributedCount = distributeTotalCount;
            }

            double d = 1.00 / (double) distributeRowCount;


            for (int i = 0; i < distributeRowCount; i++) {
                distributedPotentialList.add(1);
            }

            for (int i = 1; i <= distributedCount; i++) {
                double r1 = r.nextDouble();
                if (r1 == 0) {
                    r1 = 0.1;
                }
                int distribute = (int) Math.ceil(r1 / d) - 1;
                distributedPotentialList.set(distribute, distributedPotentialList.get(distribute) + 1);
            }
        }

        int visionCount;
        if (lAsMainPotential) {
            visionCount = totalCount - level;
        } else {
            visionCount = totalCount;
        }

        List<Integer> visionPotentialList = new ArrayList<>();

        for(int i = 0; i < visionCount; i++) {
            double r2 = r.nextDouble();
            if (r2 < visionPercent) {
                visionPotentialList.add(i);
            }
        }

        List<String> potentialStringList = new ArrayList<>();
        int iterator = 0;

        if (lAsMainPotential) {
            StringBuilder mSB = new StringBuilder();
            for (int i = 0; i < column; i++) {
                if (i < level) {
                    if (visionPotentialList.contains(iterator)) {
                        mSB.append("#");
                    } else {
                        mSB.append("=");
                    }
                } else if (visionPotentialList.contains(iterator)) {
                    mSB.append("*");
                } else {
                    mSB.append("_");
                }
                iterator++;
            }
            potentialStringList.add(mSB.toString());
        }

        for (int i = 0; i < distributeRowCount; i++) {
            StringBuilder dSB = new StringBuilder();
            for (int j = 0; j < column; j++) {
                if (j < distributedPotentialList.get(i)) {
                    if (visionPotentialList.contains(iterator)) {
                        dSB.append("#");
                    } else {
                        dSB.append("=");
                    }
                } else if (visionPotentialList.contains(iterator)) {
                    dSB.append("*");
                } else {
                    dSB.append("_");
                }
                iterator++;
            }
            potentialStringList.add(dSB.toString());
        }
        return potentialStringList;
    }
}
