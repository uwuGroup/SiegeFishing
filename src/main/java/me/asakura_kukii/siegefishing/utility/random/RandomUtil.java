package me.asakura_kukii.siegefishing.utility.random;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomUtil {
    private static final Random random = new Random();
    public static <T extends WeightedRandom> T weightedRandom(List<T> list){
        int sum = list.stream().mapToInt(WeightedRandom::getRandomWeight)
                .sum();
        if (sum == 0) {
            if (list.size() > 0) return list.get(0);
            else return null;
        }
        int selected = random.nextInt(sum);
        Iterator<Integer> iterator = list.stream().mapToInt(WeightedRandom::getRandomWeight).iterator();
        int count = 0;
        int selectedItem = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            int nextCount = count + next;
            if (count <= selected && nextCount > selected) {
                return list.get(selectedItem);
            }
            count = nextCount;
            selectedItem++;
        }
        return list.get(list.size() - 1);
    }
}
