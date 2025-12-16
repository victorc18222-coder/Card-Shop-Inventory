package service;

import model.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

public class InventoryManager {

    private final ArrayList<Card> cards = new ArrayList<>();

    // HashMap
    private final HashMap<String, Card> cardMap = new HashMap<>();

    private int nextId = 1;

    // Queue
    private final Queue<SaleRequest> checkoutQueue = new LinkedList<>();

    private static class SaleRequest {
        String cardId;
        int quantity;

        SaleRequest(String cardId, int quantity) {
            this.cardId = cardId;
            this.quantity = quantity;
        }
    }

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
        cardMap.put(card.getId(), card);
    }

    public boolean removeById(String id) {
        Card c = cardMap.remove(id);
        if (c == null) return false;
        cards.remove(c);
        return true;
    }

    public boolean updateQuantity(String id, int newQty) {
        Card c = cardMap.get(id);
        if (c == null) return false;
        c.setQuantity(newQty);
        return true;
    }

    public Card findById(String id) {
        return cardMap.get(id);
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

    // Sorts
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
        cardMap.clear();

        for (Card c : loaded) {
            cards.add(c);
            cardMap.put(c.getId(), c);
        }
    }

    public List<Card> filterByCategory(String category) {
        ArrayList<Card> results = new ArrayList<>();
        for (Card c : cards) {
            if (c.getCategory().equalsIgnoreCase(category)) {
                results.add(c);
            }
        }
        return results;
    }

    // Queue
    public boolean enqueueSale(String cardId, int qty) {
        if (qty <= 0) return false;
        Card c = cardMap.get(cardId);
        if (c == null) return false;

        checkoutQueue.add(new SaleRequest(cardId, qty));
        return true;
    }

    public String processNextSale() {
        SaleRequest req = checkoutQueue.poll();
        if (req == null) return "Checkout queue empty.";

        Card c = cardMap.get(req.cardId);
        if (c == null) return "Card not found.";

        if (c.getQuantity() < req.quantity) {
            return "Not enough stock.";
        }

        c.setQuantity(c.getQuantity() - req.quantity);
        return "Sold " + req.quantity + " of " + c.getName();
    }

    public int checkoutQueueSize() {
        return checkoutQueue.size();
    }
}
