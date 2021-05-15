package cx.rain.mc.savemyenchantments.listener;

import cx.rain.mc.savemyenchantments.utility.GrindstoneHelper;
import cx.rain.mc.savemyenchantments.utility.Tuple;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ListenerInventoryClick implements Listener {
    @EventHandler
    public void onTransferBook(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();

        if (inv == null) {
            return;
        }
        if (event.getView().getType() != InventoryType.GRINDSTONE) {
            return;
        }

        GrindstoneInventory grindstone = (GrindstoneInventory) event.getView().getTopInventory();

        if (event.isShiftClick()) { // On Shift clicked.
            // Whether player clicked their inventory or clicked grind stone inventory.
            if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
                ItemStack current = event.getCurrentItem();
                if (current != null) {
                    if (current.getType() == Material.BOOK) {
                        current.setAmount(current.getAmount() - 1);
                        ItemStack currentCopy = current.clone();
                        currentCopy.setAmount(1);
                        grindstone.setItem(1, currentCopy);

                        event.setCancelled(true);
                    }
                }
            } else {
                int rawSlot = event.getRawSlot();
                if (rawSlot == 1) {
                    event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
                    event.getClickedInventory().setItem(rawSlot, null);

                    event.setCancelled(true);
                }
            }
        } else if (event.isLeftClick() || event.isRightClick()) {  // On left or right clicked.
            if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE) {
                int rawSlot = event.getRawSlot();
                if (rawSlot == 1) {
                    ItemStack cursor = event.getCursor();
                    if (cursor != null) {
                        if (cursor.getType() == Material.BOOK) {
                            ItemStack cursorCopy = cursor.clone();
                            cursorCopy.setAmount(1);
                            cursor.setAmount(cursor.getAmount() - 1);
                            event.setCurrentItem(cursorCopy);
                            event.setCursor(cursor);

                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

        GrindstoneHelper.update(grindstone);
    }

    @EventHandler
    public void onPickupDisenchanted(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();

        if (inv == null) {
            return;
        }

        if (event.getView().getType() != InventoryType.GRINDSTONE) {
            return;
        }

        GrindstoneHelper.update((GrindstoneInventory) event.getView().getTopInventory());

        if (!(inv instanceof GrindstoneInventory)) {
            return;
        }

        if (!event.getWhoClicked().hasPermission("savemyenchantments.use")) {
            return;
        }

        GrindstoneInventory grindstone = (GrindstoneInventory) inv;
        if (event.getRawSlot() == 2) {
            if (event.getCurrentItem() != null) {
                if (grindstone.getItem(0) != null) {
                    if (GrindstoneHelper.canDisenchant(grindstone.getItem(0))) {
                        Tuple<ItemStack, ItemStack> result = GrindstoneHelper.doDisenchantment(grindstone);

                        if (result != null) {
                            if (event.isShiftClick()) {
                                event.getWhoClicked().getInventory().addItem(result.left);
                            } else if (event.isLeftClick()) {
                                event.setCursor(result.left);
                            } else if (event.isRightClick()) {
                                event.setCursor(result.left);
                            }

                            grindstone.setItem(0, null);
                            grindstone.setItem(1, result.right);
                            grindstone.setItem(2, null);

                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
