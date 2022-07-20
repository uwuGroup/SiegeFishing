package me.asakura_kukii.siegefishing.handler.nonitem.chunk;

import me.asakura_kukii.siegefishing.handler.item.fish.FishData;
import me.asakura_kukii.siegefishing.utility.coodinate.Image2D;

import java.util.HashMap;

public class ChunkData {
    public Image2D distribution;
    public HashMap<FishData, Double> fishPercentageMap = new HashMap<>();
}
