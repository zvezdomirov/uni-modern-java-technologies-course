package bg.sofia.uni.fmi.mjt.christmas;

import java.util.Random;

public enum Gift {

    BIKE("Bicycle", 50), CAR("Car", 10),
    DOLL("Barbie doll", 6), PUZZLE("Puzzle", 15);

    private static Gift[] gifts = Gift.values();
    private static Random giftRand = new Random();
    private final String type;
    private final int craftTime;

    Gift(String type, int craftTime) {
        this.type = type;
        this.craftTime = craftTime;
    }

    public static Gift getGift() {
        return gifts[giftRand.nextInt(gifts.length)];
    }

    public String getType() {
        return type;
    }

    public int getCraftTime() {
        return craftTime;
    }

}