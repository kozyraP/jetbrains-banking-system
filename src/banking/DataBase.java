package banking;

import java.util.List;

public interface DataBase {

    Account createAccount(String generateUniqueNumber, String generatePin);

    List<Account> getAccounts();

    Account findAccount(final String creditCardNumber);

    Account updateAccount(String cardNumber, long value);

    boolean deleteAccount(final Account account);
}
