package banking;

public class Account {
    private String cardNumber;
    private String pin;
    private long balance;

    protected Account(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        balance = 0;
    }

    public Account(String cardNumber, String pin, long balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public long getBalance() {
        return balance;
    }

    public void changeBalance(long income) {
        if (income > 0) {
            balance += income;
        }
    }
}
