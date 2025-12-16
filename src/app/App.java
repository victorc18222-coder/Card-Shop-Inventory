package app;

import model.Card;
import service.InventoryManager;
import util.CsvStorage;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final String DATA_FILE = "data/inventory.csv";

    public static void main(String[] args) {
        InventoryManager inventory = new InventoryManager();
        loadInventory(inventory);

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Card Shop Inventory ===");
            System.out.println("1) Add card");
            System.out.println("2) Update quantity");
            System.out.println("3) Remove card");
            System.out.println("4) Search");
            System.out.println("5) View all");
            System.out.println("6) Pokemon section");
            System.out.println("7) Sports section");
            System.out.println("8) Low stock");
            System.out.println("9) Save & exit");
            System.out.print("Choice: ");

            String input = sc.nextLine().trim();

            switch (input) {
                case "1" -> addCard(sc, inventory);
                case "2" -> updateQuantity(sc, inventory);
                case "3" -> removeCard(sc, inventory);
                case "4" -> search(sc, inventory);
                case "5" -> viewAll(sc, inventory);
                case "6" -> viewCategory(sc, inventory, "Pokemon");
                case "7" -> viewCategory(sc, inventory, "Sports");
                case "8" -> lowStock(sc, inventory);
                case "9" -> {
                    saveInventory(inventory);
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addCard(Scanner sc, InventoryManager inv) {
        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Set: ");
        String set = sc.nextLine();

        System.out.println("Category: 1) Pokemon  2) Sports  3) Other");
        String cChoice = sc.nextLine().trim();
        String category;
        if (cChoice.equals("1")) category = "Pokemon";
        else if (cChoice.equals("2")) category = "Sports";
        else category = "Other";

        System.out.print("Price: ");
        double price = readDouble(sc);

        System.out.print("Quantity: ");
        int qty = readInt(sc);

        String id = inv.generateId();
        inv.addCard(new Card(id, name, set, category, price, qty));
        System.out.println("Added card " + id);
    }

    private static void updateQuantity(Scanner sc, InventoryManager inv) {
        System.out.print("Card ID: ");
        String id = sc.nextLine().trim();

        Card c = inv.findById(id);
        if (c == null) {
            System.out.println("Card not found.");
            return;
        }

        System.out.println("Found: " + c);
        System.out.print("New quantity: ");
        int qty = readInt(sc);

        if (qty < 0) {
            System.out.println("Quantity can't be negative.");
            return;
        }

        inv.updateQuantity(id, qty);
        System.out.println("Updated.");
    }

    private static void removeCard(Scanner sc, InventoryManager inv) {
        System.out.print("Card ID: ");
        String id = sc.nextLine().trim();

        if (inv.removeById(id)) System.out.println("Removed.");
        else System.out.println("Card not found.");
    }
     private static void search(Scanner sc, InventoryManager inv) {
        System.out.print("Search name: ");
        String q = sc.nextLine();

        List<Card> results = inv.searchByName(q);
        if (results.isEmpty()) {
            System.out.println("No results.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private static void viewAll(Scanner sc, InventoryManager inv) {
        List<Card> list = inv.getAll();

        if (list.isEmpty()) {
            System.out.println("Inventory empty.");
            return;
        }

        sortMenu(sc, inv, list);
        list.forEach(System.out::println);
    }

    private static void viewCategory(Scanner sc, InventoryManager inv, String category) {
        List<Card> list = inv.filterByCategory(category);

        if (list.isEmpty()) {
            System.out.println("Nothing in " + category + " yet.");
            return;
        }

        System.out.println("\n--- " + category + " ---");
        sortMenu(sc, inv, list);
        list.forEach(System.out::println);
    }

    private static void lowStock(Scanner sc, InventoryManager inv) {
        System.out.print("Threshold: ");
        int t = readInt(sc);

        if (t < 0) {
            System.out.println("Threshold can't be negative.");
            return;
        }

        List<Card> low = inv.lowStock(t);
        if (low.isEmpty()) {
            System.out.println("No low stock items.");
        } else {
            low.forEach(System.out::println);
        }
    }

    private static void sortMenu(Scanner sc, InventoryManager inv, List<Card> list) {
        System.out.println("Sort by: 1) Name  2) Quantity  3) Price  (enter = no sort)");
        String choice = sc.nextLine().trim();

        if (choice.equals("1")) inv.sortByName(list);
        else if (choice.equals("2")) inv.sortByQuantity(list);
        else if (choice.equals("3")) inv.sortByPrice(list);
    }

    private static void saveInventory(InventoryManager inv) {
        try {
            CsvStorage.save(DATA_FILE, inv.getAll());
            System.out.println("Saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private static void loadInventory(InventoryManager inv) {
        try {
            List<Card> loaded = CsvStorage.load(DATA_FILE);
            inv.clearAndLoad(loaded);
            inv.setNextIdFromExisting();
            if (!loaded.isEmpty()) System.out.println("Loaded " + loaded.size() + " items.");
        } catch (IOException ignored) {
            // no file yet, start empty
        }
    }

    private static int readInt(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Enter a whole number: ");
            }
        }
    }

    private static double readDouble(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }
}




