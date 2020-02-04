package bg.sofia.uni.fmi.mjt.virtualwallet.core.payment;

import java.time.LocalDateTime;

public class TransactionInfo {
    private String cardName;
    private LocalDateTime transactionDate;
    private PaymentInfo paymentInfo;

    public TransactionInfo(String cardName, LocalDateTime transactionDate, PaymentInfo paymentInfo) {
        this.cardName = cardName;
        this.transactionDate = transactionDate;
        this.paymentInfo = paymentInfo;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}
