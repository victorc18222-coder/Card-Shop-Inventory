package service;

import model.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InventoryManager {
    private final ArrayList<Card> cards = new ArrayList<>();
    private int nextId = 1;

    public void setNextIdFromExisting() {
        int max = 0;
        for (Card c : cards) {
            String digits = c.getId().replaceAll("[^0-9]", "");
            if (!digits.isEmpty()) {
                try {
                    max = Math.max(max, Integer.parseInt(digits));
                } catch (NumberFormatException ignored) {}
            }
        }
        nextId = max + 1;
    }

    public String generateId() {
        return String.format("C%04d", nextId++);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public boolean removeById(String id) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getId().equalsIgnoreCase(id)) {
                cards.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean updateQuantity(String id, int newQty) {
        Card c = findById(id);
        if (c == null) return false;
        c.setQuantity(newQty);
        return true;
    }

    public Card findById(String id) {
        for (Card c : cards) {
            if (c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    public List<Card> searchByName(String query) {
        ArrayList<Card> results = new ArrayList<>();
        String q = query.toLowerCase();

        for (Card c : cards) {
            if (c.getName().toLowerCase().contains(q)) {
                results.add(c);
            }
        }
        return results;
    }
     public List<Card> getAll() {
        return new ArrayList<>(cards);
    }

    public List<Card> lowStock(int threshold) {
        ArrayList<Card> results = new ArrayList<>();
        for (Card c : cards) {
            if (c.getQuantity() <= threshold) {
                results.add(c);
            }
        }
        return results;
    }

    public void sortByName(List<Card> list) {
        list.sort(Comparator.comparing(Card::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public void sortByQuantity(List<Card> list) {
        list.sort(Comparator.comparingInt(Card::getQuantity).reversed());
    }

    public void sortByPrice(List<Card> list) {
        list.sort(Comparator.comparingDouble(Card::getPrice).reversed());
    }

    public void clearAndLoad(List<Card> loaded) {
        cards.clear();
        cards.addAll(loaded);
    }

    // NEW: filter section (Pokemon / Sports)
    public List<Card> filterByCategory(String category) {
        ArrayList<Card> results = new ArrayList<>();
        for (Card c : cards) {
            if (c.getCategory().equalsIgnoreCase(category)) {
                results.add(c);
            }
        }
        return results;
    }
}