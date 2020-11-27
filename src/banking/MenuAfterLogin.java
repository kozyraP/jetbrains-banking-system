package banking;

import java.util.Scanner;

public class MenuAfterLogin {
    private Scanner scanner = new Scanner(System.in);
    private AccountManager accountManager;
    private boolean shouldExit = false;

    public MenuAfterLogin(AccountManager accountManager) {
        this.accountManager = accountManager;
        logIntoAcc();
    }

    public void logIntoAcc() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();
        Account account = accountManager.findAccountByCardNr(cardNumber);
        if (account == null) {
            System.out.println("Account does not exist");
        } else if (pin.equals(account.getPin())) {
            System.out.println("You have successfully logged in!");


            while (true) {
                System.out.println(String.join("\n",
                        "1. Balance",
                        "2. Add income",
                        "3. Do transfer",
                        "4. Close account",
                        "5. Log out",
                        "0. Exit"));
                int input = scanner.nextInt();
                switch (input) {
                    case 1:
                        performGetBalance(cardNumber);
                        continue;
                    case 2:
                        performAddIncome(cardNumber);
                        continue;
                    case 3:
                        performMoneyTransfer(cardNumber);
                        continue;
                    case 4:
                        if (accountManager.deleteAccount(cardNumber)) {
                            System.out.println("The account has been closed!");
                        } else {
                            System.out.println("Error! Can't close this account! ");
                            continue;
                        }
                        break;
                    case 5:
                        System.out.println("You have successfully logged out!");
                        break;
                    case 0:
                        shouldExit = true;
                        break;
                }
                break;
            }
        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }

    private void performMoneyTransfer(String cardNumber) {
        System.out.println("Enter card number: ");
        String receiverCard = scanner.next();
        if (receiverCard.equals(cardNumber)) {
            System.out.println("You can't transfer money to the same account! ");
            return;
        } else if (!accountManager.checkLuhn(receiverCard)) {
            System.out.println("Probably you made mistake in the card number. Please try again! ");
            return;
        } else if (accountManager.findAccountByCardNr(receiverCard) == null) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer: ");
        long amountTooBeSent = Long.parseLong(scanner.next());

        if (accountManager.getBalance(cardNumber) < amountTooBeSent) {
            System.out.println("Not enough money!");
        } else {
            transferMoney(cardNumber, receiverCard, amountTooBeSent);
            System.out.println("Success! ");
        }
    }

    private void transferMoney(String senderCard, String receiverCard, long amountTooBeSent) {
        accountManager.makeTransfer(senderCard, receiverCard, amountTooBeSent);
    }

    private void performGetBalance(String cardNumber) {
        System.out.printf("Balance: %d\n", accountManager.getBalance(cardNumber));
    }

    private void performAddIncome(String cardNumber) {
        System.out.println("Enter income: ");
        long income = Long.parseLong(scanner.next());
        accountManager.addIncome(cardNumber, income);
        if (income > 0) {
            System.out.println("Income was added!");
        } else {
            System.out.println("Income must be positive natural number");
        }
    }

    public boolean shouldExit() {
        return shouldExit;
    }
}
