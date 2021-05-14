package cx.rain.mc.savemyenchantments.listener;

import cx.rain.mc.savemyenchantments.utility.GrindstoneHelper;
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

        if (event.isShiftClick()) {
            if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
                Inventory grindstone = event.getView().getTopInventory();
                grindstone.setItem(2, event.getCurrentItem());
            } else {
                event.getClickedInventory().setItem(1, null);
                event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
            }
            event.setCurrentItem(null);

            GrindstoneHelper.update((GrindstoneInventory) event.getView().getTopInventory());
            return;
        }

        if (event.isLeftClick()) {
            if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE) {
                ItemStack cursor = event.getCursor();
                if (cursor != null) {
                    if (cursor.getType() == Material.BOOK) {
                        if (event.getRawSlot() == 1) {
                            event.setCancelled(true);

                            event.setCurrentItem(cursor);
                            event.setCursor(null);
                        }
                    }
                }
            }

            GrindstoneHelper.update((GrindstoneInventory) event.getView().getTopInventory());
            return;
        }

        if (event.isRightClick()) {
            if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE) {
                ItemStack cursor = event.getCursor();
                if (cursor == null) {
                    if (event.getRawSlot() == 1) {
                        ItemStack stack = event.getClickedInventory().getItem(1);
                        if (stack != null && stack.getAmount() > 0) {
                            stack.setAmount(stack.getAmount() / 2);
                            event.setCurrentItem(stack);

                            ItemStack stack1 = stack.clone();
                            stack1.setAmount(stack.getAmount() - stack.getAmount() / 2);
                            event.setCursor(stack1);
                        }
                    }
                } else {
                    if (cursor.getType() == Material.BOOK) {
                        if (event.getRawSlot() == 1) {
                            cursor.setAmount(cursor.getAmount() - 1);
                            ItemStack stack = event.getCurrentItem();
                            if (stack != null) {
                                stack.setAmount(stack.getAmount() + 1);
                            } else {
                                stack = new ItemStack(Material.BOOK);
                            }
                            event.setCurrentItem(stack);
                        }
                    }
                }

                GrindstoneHelper.update((GrindstoneInventory) event.getView().getTopInventory());
                event.setCancelled(true);
            }
        }
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
            if (GrindstoneHelper.canDisenchant(event.getCurrentItem())) {
                ItemStack result = GrindstoneHelper.doDisenchantment(grindstone);

                if (event.isShiftClick()) {
                    event.getWhoClicked().getInventory().addItem(result);
                } else if (event.isLeftClick()) {
                    event.setCursor(result);
                } else if (event.isRightClick()) {
                    event.setCursor(result);
                }
            }
        }
    }
}
