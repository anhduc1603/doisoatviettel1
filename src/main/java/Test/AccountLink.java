package Test;

public class AccountLink {
    private String accountLive;
    private String cardIdLive;

    public String getAccountLive() {
        return accountLive;
    }

    public void setAccountLive(String accountLive) {
        this.accountLive = accountLive;
    }

    public String getCardIdLive() {
        return cardIdLive;
    }

    public void setCardIdLive(String cardIdLive) {
        this.cardIdLive = cardIdLive;
    }

    public AccountLink(String accountLive, String cardIdLive) {
        this.accountLive = accountLive;
        this.cardIdLive = cardIdLive;
    }
}
