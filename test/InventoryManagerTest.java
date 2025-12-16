package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import service.InventoryManager;
import model.Card;

public class InventoryManagerTest {

    @Test
    void addCard_thenFindById() {
        InventoryManager inv = new InventoryManager();
        String id = inv.generateId();

        inv.addCard(new Card(id, "Pikachu", "CZ", "Pokemon", 3.5, 5));

        Card found = inv.findById(id);
        assertNotNull(found);
        assertEquals("Pikachu", found.getName());
    }

    @Test
    void updateQuantity_changesQuantity() {
        InventoryManager inv = new InventoryManager();
        String id = inv.generateId();

        inv.addCard(new Card(id, "Charizard", "Base", "Pokemon", 200, 1));
        assertTrue(inv.updateQuantity(id, 10));

        assertEquals(10, inv.findById(id).getQuantity());
    }

    @Test
    void queueProcessesInOrder() {
        InventoryManager inv = new InventoryManager();
        String id = inv.generateId();

        inv.addCard(new Card(id, "Pikachu", "CZ", "Pokemon", 3.5, 10));

        assertTrue(inv.enqueueSale(id, 2));
        assertTrue(inv.enqueueSale(id, 3));

        inv.processNextSale();
        assertEquals(8, inv.findById(id).getQuantity());

        inv.processNextSale();
        assertEquals(5, inv.findById(id).getQuantity());
    }
}
