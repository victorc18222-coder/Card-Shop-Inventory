package model;

public class Card {
    private final String id;
    private String name;
    private String setName;
    private String category;
    private double price;
    private int quantity;

    public Card(String id, String name, String setName, String category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.setName = setName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSetName() { return setName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setName(String name) { this.name = name; }
    public void setSetName(String setName) { this.setName = setName; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "[" + id + "] " + name +
                " | Set: " + setName +
                " | Category: " + category +
                " | $" + String.format("%.2f", price) +
                " | Qty: " + quantity;
    }
}
