package bg.sofia.uni.fmi.mjt.christmas;

public class Elf extends Thread {
    private int id;
    private Workshop workshop;
    private int totalGiftsCrafted;

    public Elf(int id, Workshop workshop) {
        this.id = id;
        this.workshop = workshop;
        this.totalGiftsCrafted = 0;
    }

    /**
     * Gets a wish from the backlog and creates the wanted gift.
     **/
    public void craftGift() {
        Gift giftToCraft = this.workshop.nextGift();
        try {
            Thread.sleep(giftToCraft.getCraftTime());
        } catch (InterruptedException ie) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();
        }
        this.totalGiftsCrafted++;
    }

    /**
     * Returns the total number of gifts that the given elf has crafted.
     **/
    public int getTotalGiftsCrafted() {
        return this.totalGiftsCrafted;
    }

    @Override
    public void run() {
        do {
            this.craftGift();
        } while (this.workshop.getQueryingWishesCount() != 0);
    }

    public int getElfId() {
        return id;
    }
}