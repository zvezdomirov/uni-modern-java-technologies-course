package bg.sofia.uni.fmi.mjt.christmas;

public class Kid extends Thread {
    private Workshop workshop;

    public Kid(Workshop workshop) {
        this.workshop = workshop;
    }

    @Override
    public void run() {
        final int timeToChooseGift = 500;
        try {
            // Simulate the wait to choose a gift
            Thread.sleep(timeToChooseGift);
            this.workshop.postWish(
                    Gift.getGift());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}