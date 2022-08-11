package me.asakura_kukii.siegefishing.handler.method.fishing;

import me.asakura_kukii.siegefishing.config.data.addon.FishData;
import me.asakura_kukii.siegefishing.handler.method.fishing.ai.FishAI;
import me.asakura_kukii.siegefishing.utility.random.WeightedRandom;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SiegeFish implements WeightedRandom {
    private FishAI fishAI;
    private FishData fishData;
    /**
     *  weight in random selecting, not fish weight!
     *  USE WITH CARE!!!
     */
    private int randomWeight;
    private int ticksOfHook;

    public SiegeFish(FishData fishData, int randomWeight, int ticksOfHook){
        this.fishAI = buildFishAi(fishData);
        this.fishData = fishData;

        // todo: finish this
//        this.fishAI = new FishAI();
        this.randomWeight = randomWeight;
        this.ticksOfHook = ticksOfHook;
    }

    private static final Random random = new Random();
    private FishAI buildFishAi(FishData fishData) {
        double fishSize = (fishData.weightMax * random.nextGaussian() + fishData.weightMin);
        double speed = fishSize * 5;
        double hp = fishSize * 20 + 100;
        double strength = fishData.difficulty;
        return new FishAI(speed, hp, strength);
    }

    public void doTick(FishingSession fishingSession, long tick) {
        fishAI.doAI(fishingSession, tick);
    }

    public ItemStack getFishItem(){
        return new ItemStack(Material.AIR);
    }

    /**
     *  weight in random selecting, not fish weight!
     *  USE WITH CARE!!!
     */
    @Override
    public int getRandomWeight() {
        return randomWeight;
    }

    public int getHookTime() {
        return ticksOfHook;
    }

    public double getHp() {
        return fishAI.getHp();
    }
}
