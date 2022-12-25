package com.kyuuzinbr.gldcmod.util;

import java.util.Random;

public class MathUtil {
    public static boolean randomChance(int minVal, int maxVal, int reqVal) {
        Random random = new Random();
        int randomNumber = random.nextInt(maxVal - minVal) + minVal;
        return randomNumber >= reqVal;
    }
}
