package Test;

public class Account {
    public String accountNumber;
    public String cardId;

    public Account() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Account(String accountNumber, String cardId) {
        this.accountNumber = accountNumber;
        this.cardId = cardId;
    }
}
