package banking;

import java.util.Random;

public class AccountManager {
    private final String BIN = "400000";
    private final DataBase dataBase;

    public AccountManager(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    protected Account createAccount() {
        return dataBase.createAccount(generateUniqueNumber(), generatePin(4));
    }

    protected Account findAccountByCardNr(String cn) {
        for (Account account : dataBase.getAccounts()) {
            if (account.getCardNumber().equals(cn)) {
                return account;
            }
        }
        return null;
    }

    protected String generateUniqueNumber() {
        String result = generateNumber(9).toString();
        Account account = findAccountByCardNr(BIN + result);
        while (account != null) {
            result = generateNumber(9).toString();
            account = findAccountByCardNr(BIN + result);
        }
        return BIN + result;
    }

    private StringBuilder generateNumber(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(new Random().nextInt(10));
        }
        stringBuilder.append(checksum(stringBuilder.toString()));
        return stringBuilder;
    }

    protected String generatePin(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(new Random().nextInt(10));
        }
        return stringBuilder.toString();
    }

    private String checksum(String number) {
        String cardNrWithoutLast = BIN + number;
        int[] temp = new int[cardNrWithoutLast.length()];
        for (int i = 0; i < cardNrWithoutLast.length(); i++) {
            if (i % 2 == 0) {
                temp[i] = Character.getNumericValue(cardNrWithoutLast.charAt(i)) * 2;
            } else {
                temp[i] = Character.getNumericValue(cardNrWithoutLast.charAt(i));
            }
        }
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] > 9) {
                temp[i] -= 9;
            }
        }
        int sumOfAllNums = 0;
        for (int i : temp) {
            sumOfAllNums += i;
        }
        return String.valueOf((((sumOfAllNums + 9) / 10) * 10) - sumOfAllNums);
    }

    public long getBalance(String cardNumber) {
        return findAccountByCardNr(cardNumber).getBalance();
    }

    public void addIncome(String cardNumber, long income) {
        dataBase.updateAccount(cardNumber, income);
    }

    public void makeTransfer(String from, String to, long income) {
        dataBase.updateAccount(from, -income);
        dataBase.updateAccount(to, income);
    }

    public boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNo.charAt(i) - '0';
            if (isSecond) {
                d = d * 2;
            }
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    public boolean deleteAccount(String cardNumber) {
        return dataBase.deleteAccount(findAccountByCardNr(cardNumber));
    }
}
