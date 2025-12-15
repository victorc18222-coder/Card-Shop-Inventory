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