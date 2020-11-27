package banking;

import java.util.Scanner;

public class Application {
    private Scanner scanner = new Scanner(System.in);
    private AccountManager accountManager;

    public Application(DataBase dataBase) {
        accountManager = new AccountManager(dataBase);
    }

    public void start() {
        int userInput;
        while (true) {
            printMenu();
            userInput = getMenuItem();

            if (userInput == 1) {
                printCardInfoAfterCreation(accountManager.createAccount());
            }

            else if (userInput == 2) {
                boolean shouldExit = new MenuAfterLogin(accountManager).shouldExit();
                if (shouldExit) {
                    break;
                }
            } else if (userInput == 0) {
                System.out.println("Bye!");
                break;
            }
        }
    }

    private void printMenu() {
        System.out.println(
                        "1. Create an account\n" +
                        "2. Log into account\n" +
                        "0. Exit");
    }

    private void printCardInfoAfterCreation(Account account) {
        System.out.printf(
                "Your card has been created\n" +
                "Your card number:\n" +
                "%s\n" +
                "Your card PIN:\n" +
                "%s\n",
                account.getCardNumber(), account.getPin());
    }
    private int getMenuItem() {
        return Integer.parseInt(scanner.nextLine());
    }
}
