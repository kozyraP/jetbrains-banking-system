package banking;

import java.util.ArrayList;
import java.util.List;

class InMemoryDB implements DataBase {
    private List<Account> accounts = new ArrayList<>();

    @Override
    public Account createAccount(String cardNumber, String pin) {
        Account account = new Account(cardNumber, pin);
        accounts.add(account);
        return account;
    }

    @Override
    public List<Account> getAccounts() {
        return accounts;
    }


    @Override
    public boolean deleteAccount(Account account) {
        return accounts.remove(account);
    }

    @Override
    public Account findAccount(String creditCardNumber) {
        for (Account account : accounts) {
            if (account.getCardNumber().equals(creditCardNumber)) {
                return account;
            }
        }
        return null;
    }

    @Override
    public Account updateAccount(String cardNumber, long value) {
        findAccount(cardNumber).changeBalance(value);
        return findAccount(cardNumber);
    }
}


