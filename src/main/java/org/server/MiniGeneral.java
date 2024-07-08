package org.server;

import java.util.Random;

public class MiniGeneral {
    private static final Random random = new Random();

    public static int[] rollDice() {
        return new int[]{random.nextInt(6) + 1, random.nextInt(6) + 1, random.nextInt(6) + 1};
    }

    public static void main(String[] args) {
        int[] dice = rollDice();
        System.out.println("Rolled dice: " + dice[0] + ", " + dice[1] + ", " + dice[2]);
    }
}
