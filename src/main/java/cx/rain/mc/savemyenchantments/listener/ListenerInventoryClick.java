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
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();

        if (inv == null) {
            return;
        }
        
        if (event.getView().getType() != InventoryType.GRINDSTONE) {
            return;
        }

        if (!(inv instanceof GrindstoneInventory)) {
            return;
        }

        if (!event.getWhoClicked().hasPermission("savemyenchantments.use")) {
            return;
        }

        GrindstoneInventory grindstone = (GrindstoneInventory) event.getClickedInventory();

        ItemStack cursor = event.getCursor();
        if (cursor != null) {
            if (cursor.getType() == Material.BOOK) {
                if (event.getRawSlot() == 1) {
                    event.setCancelled(true);

                    event.setCurrentItem(cursor);
                    event.setCursor(null);

                    GrindstoneHelper.update(grindstone);
                }
            }
        } else {
            if (event.getRawSlot() == 2) {
                event.setCursor(GrindstoneHelper.doDisenchantment(grindstone));
            }
        }
    }
}
