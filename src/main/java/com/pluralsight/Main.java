package com.pluralsight;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create a scanner
        Scanner scanner = new Scanner(System.in);

        // Inventory ArrayList of every item inside inventory
        Map<Integer,Product> inventory = new HashMap<>();

        // Collect all existing items inside of inventory.csv
        getInventory(inventory,"inventory.csv");

        // Run the inventory search application
        runApp(inventory,scanner);

        // Close the scanner
        scanner.close();
    }

    public static void runApp(Map<Integer,Product> inventory,Scanner scanner) {
        boolean appRunning = true;
        while (appRunning) {
            // Open the menu
            openMenu();
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("a")) {
                // Read all the items in the inventory
                listInventory(inventory);
            } else if (response.equalsIgnoreCase("b")) {
                // Add to the inventory
                addToInventory(inventory,scanner);}
            else if (response.equalsIgnoreCase("c")) {
                // Search for item by name
                promptSearch(inventory,scanner,"-> Enter item name to search: ");
            } else if (response.equalsIgnoreCase("d")) {
                // Exit the app
                System.out.println("-> Exiting the app...");
                appRunning = false;
            } else {
                // Error, invalid input
                System.out.println("Error: Invalid input");
            }
        }
    }

    public static void promptSearch(Map<Integer,Product> inventory, Scanner scanner, String question) {
        System.out.println("-> Enter item name: ");
        String search = scanner.nextLine().trim();

        Product searchedProduct = searchInventory(inventory,search);
        if (searchedProduct != null) {
            System.out.println("\n[SEARCH RESULTS]");

        } else {
            System.out.println("Error: Could not find search query");
        }

        System.out.println("Would you like to search again?");
    }

    public static Product searchInventory(Map<Integer,Product> inventory, String search) {
        // Loop inventory from keys to get values in loop
        for (int id : inventory.keySet()) {
            // Search for name math from search query
            Product product = inventory.get(id);
            if (product.getName().equalsIgnoreCase(search)) {
                // We found the product from the search
                return product;
            }
        }
        // We didn't find the search
        return null;
    }

    public static void listInventory(Map<Integer,Product> inventory) {
        System.out.println("\n[INVENTORY PRODUCT INFORMATION]");
        // Display the items in the inventory
        for (Product product : inventory.values()) {
            System.out.printf(
                    "id: %d, name: %s, price: $%.2f\n",
                    product.getId(),product.getName(),product.getPrice()
            );
        }
        System.out.println();
    }

    public static void addToInventory(Map<Integer,Product> inventory,Scanner scanner) {
        // Prompt the user for the info needed to create a Product
        System.out.println("\n[ADD ITEM TO INVENTORY]");

        System.out.println("-> Enter item id (ex. 4221:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("-> Enter item name:");
        String name = scanner.nextLine();

        System.out.println("-> Enter item price:");
        double price = scanner.nextDouble();
        scanner.nextLine();

        // Create the product & add to inventory
        Product product = new Product(id,name,price);
        inventory.put(id,product);

        // Append product information to the .csv file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory.csv",true))) {
            String line = String.valueOf(id) + "|" + name + "|" + String.valueOf(price);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            // Error reading the file
            System.out.println("Error: Issue reading the file");
        }

        System.out.println("\n-> Created new product! (" + name + ")\n");
    }

    public static void openMenu() {
        System.out.println("[INVENTORY MENU]");
        System.out.println("-> Choose a option from the list");
        System.out.println("A.) View inventory");
        System.out.println("B.) Add to inventory");
        System.out.println("C.) Search inventory");
        System.out.println("D.) Exit");
    }

    public static void getInventory(Map<Integer,Product> inventory, String fileName) {
        // Read through the csv file and add to inventory
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Loop through every line to create product
            while ((line = reader.readLine()) != null) {
                // Split the .csv file line (id|name|price)
                String[] split = line.split("\\|");

                // Create the product, add to inventory ArrayList
                int id = Integer.parseInt(split[0]);
                String name = split[1];
                double price = Double.parseDouble(split[2]);

                Product product = new Product(id,name,price);
                inventory.put(id,product);
            }
        } catch (IOException e) {
            // Error reading the file
            System.out.println("Error: file could not be read");
        }
    }
}