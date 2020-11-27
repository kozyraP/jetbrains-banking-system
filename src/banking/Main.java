package banking;

public class Main {
    public static void main(String[] args) {
        new Application(
                args.length == 2 && "-fileName".equals(args[0]) ?
                new SQLiteDB(args[1]) :
                new InMemoryDB())
                .start();
    }
}