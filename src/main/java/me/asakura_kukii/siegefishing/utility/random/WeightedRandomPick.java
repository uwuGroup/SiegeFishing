package me.asakura_kukii.siegefishing.utility.random;

import me.asakura_kukii.siegefishing.config.data.addon.PlayerData;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WeightedRandomPick {
    public double[] weightArray;
    public double sum = 0;
    Random r = new Random();

    public WeightedRandomPick(List<Double> weight) {
        weightArray = new double[weight.size() + 1];
        int index = 1;
        for (Double d : weight) {
            sum = sum + d;
            weightArray[index] = sum;
            index++;
        }
    }

    public int getIndex() {
        double referenceDouble = r.nextDouble() * sum;
        int index = -1;
        for (Double d : weightArray) {
            if (d >= referenceDouble) {
                break;
            }
            index++;
        }
        return index;
    }
}
