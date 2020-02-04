package bg.sofia.uni.fmi.mjt.virtualwallet.core;

import bg.sofia.uni.fmi.mjt.virtualwallet.core.card.Card;
import bg.sofia.uni.fmi.mjt.virtualwallet.core.payment.PaymentInfo;
import bg.sofia.uni.fmi.mjt.virtualwallet.core.payment.TransactionInfo;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class VirtualWallet implements VirtualWalletAPI {

    public static final int MAX_WALLET_SIZE = 5;
    public static final int MAX_TRANSACTIONS_SIZE = 10;
    private Map<String, Card> wallet;
    private Queue<TransactionInfo> latestTransactions;

    public VirtualWallet() {
        this.wallet = new HashMap<>();
        this.latestTransactions = new ArrayDeque<>(MAX_TRANSACTIONS_SIZE);
    }


    @Override
    public boolean registerCard(Card card) {
        String cardName;
        if (card == null ||
                (cardName = card.getName()) == null ||
                this.wallet.containsKey(cardName) ||
                this.wallet.size() >= MAX_WALLET_SIZE) {
            return false;
        }
        this.wallet.put(cardName, card);
        return true;
    }

    @Override
    public boolean executePayment(Card card, PaymentInfo paymentInfo) {
        if (card == null || paymentInfo == null) {
            return false;
        }
        String cardName = card.getName();
        if (!wallet.containsKey(cardName)) {
            return false;
        }
        double paymentCost = paymentInfo.getCost();
        if (paymentInfo.getReason() == null ||
                paymentInfo.getLocation() == null) {
            return false;
        }
        if (card.executePayment(paymentCost)) {
            this.wallet.put(cardName, card);
            this.addTransaction(cardName, LocalDateTime.now(), paymentInfo);
            return true;
        }
        return false;
    }

    @Override
    public boolean feed(Card card, double amount) {
        if (card == null) {
            return false;
        }
        if (!wallet.containsKey(card.getName())) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        card.setAmount(card.getAmount() + amount);
        return true;
    }

    @Override
    public Card getCardByName(String name) {
        return this.wallet.get(name);
    }

    @Override
    public int getTotalNumberOfCards() {
        return this.wallet.size();
    }

    public TransactionInfo getLatestTransaction() {
        return this.latestTransactions.peek();
    }

    private void addTransaction(String cardName, LocalDateTime transactionDate,
                                PaymentInfo paymentInfo) {
        if (this.latestTransactions.size() >= MAX_TRANSACTIONS_SIZE) {
            this.latestTransactions.remove();
        }
        TransactionInfo newTransaction =
                new TransactionInfo(cardName, transactionDate, paymentInfo);
        this.latestTransactions.add(newTransaction);
    }
}
