package bg.sofia.uni.fmi.mjt.christmas;

import java.util.Arrays;

public class Main {

    public static final int KIDS_COUNT = 40;
    public static final int ELVES_COUNT = 20;

    public static void main(String[] args) {
        Workshop workshop = new Workshop();
        Kid[] kids = new Kid[KIDS_COUNT];
        Elf[] elves = workshop.getElves();
        for (int i = 0; i < KIDS_COUNT; i++) {
            kids[i] = new Kid(workshop);
            kids[i].start();
        }
        for (int i = 0; i < elves.length; i++) {
            elves[i].start();
        }
        for (int i = 0; i < ELVES_COUNT; i++) {
            try {
                kids[i].join();
                elves[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(workshop.getWishCount());
        Arrays.stream(workshop.getElves())
                .forEach(elf -> System.out.print(elf.getTotalGiftsCrafted() + " "));
    }
}
