import java.util.List;

public class GameInventoryExercise {
    // Common interface for all items
    interface ItemStats {
        int id();
        String name();
        int value();
    }

    // Base record for regular items
    record Item(int id, String name, int value) implements ItemStats {
        public Item {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (value < 0) {
                throw new IllegalArgumentException("Value must be positive");
            }
        }
    }

    // Interface for weapon-specific properties
    interface WeaponStats {
        int damage();
        int durability();
    }

    // Separate record for weapons implementing both interfaces
    record Weapon(int id, String name, int value, int damage, int durability)
            implements ItemStats, WeaponStats {
        public Weapon {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (value < 0) {
                throw new IllegalArgumentException("Value must be positive");
            }
            if (damage <= 0) {
                throw new IllegalArgumentException("Damage must be positive");
            }
            if (durability <= 0) {
                throw new IllegalArgumentException("Durability must be positive");
            }
        }
    }

    record InventorySlot(ItemStats item, int quantity) {
        public InventorySlot {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
        }
    }

    record Inventory(List<InventorySlot> slots, int capacity) {
        public Inventory {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive");
            }
            slots = List.copyOf(slots); // Make the list immutable
        }

        public boolean isFull() { return slots.size() >= capacity; }
    }

    public static void main(String[] args) {
        // Create items
        Item potion = new Item(1, "Health Potion", 50);
        Weapon sword = new Weapon(2, "Steel Sword", 100, 25, 100);

        // Create inventory slots
        InventorySlot potionSlot = new InventorySlot(potion, 5);
        InventorySlot swordSlot = new InventorySlot(sword, 1);

        // Create inventory
        Inventory inventory = new Inventory(
                List.of(potionSlot, swordSlot),
                10
        );

        // Demonstrate usage
        System.out.println("Inventory contents:");
        inventory.slots().forEach(slot -> {
            ItemStats item = slot.item();
            System.out.print("- " + item.name() + " (Quantity: " + slot.quantity() + ")");
            if (item instanceof WeaponStats weapon) {
                System.out.print(
                        " [Damage: " + weapon.damage() +
                                ", Durability: " + weapon.durability() + "]"
                );
            }
            System.out.println();
        });

        System.out.println("Inventory full: " + inventory.isFull());

        // Demonstrate equality
        Item potion2 = new Item(1, "Health Potion", 50);
        System.out.println("Potions equal: " + potion.equals(potion2));
    }
}
