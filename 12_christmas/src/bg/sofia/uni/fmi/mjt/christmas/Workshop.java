package bg.sofia.uni.fmi.mjt.christmas;

import java.util.ArrayDeque;
import java.util.Queue;

public class Workshop {
    public static final int ELVES_COUNT = 20;
    private int wishCount;
    private Queue<Gift> wishes;
    private Elf[] elves;

    public Workshop() {
        this.wishes = new ArrayDeque<>();
        this.initElves();
    }

    private void initElves() {
        this.elves = new Elf[ELVES_COUNT];
        for (int i = 0; i < ELVES_COUNT; i++) {
            this.elves[i] = new Elf(i, this);
        }
    }

    /**
     * Adds a gift to the elves' backlog.
     **/
    public synchronized void postWish(Gift gift) {
        this.wishes.offer(gift);
        this.wishCount++;
        this.notify();
    }

    /**
     * Returns an array of the elves working in Santa's workshop.
     **/
    public Elf[] getElves() {
        return this.elves;
    }

    /**
     * Returns the next gift from the elves' backlog that has to be manufactured.
     **/
    public synchronized Gift nextGift() {
        Gift nextGift = this.wishes.poll();
        while (nextGift == null) {
            try {
                this.wait();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            nextGift = this.wishes.poll();
        }
        return nextGift;
    }

    /**
     * Returns the total number of wishes sent to Santa's workshop by the kids.
     **/
    public int getWishCount() {
        return this.wishCount;
    }

    public int getQueryingWishesCount() {
        return this.wishes.size();
    }
}