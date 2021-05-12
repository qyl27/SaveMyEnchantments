package cx.rain.mc.savemyenchantments.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;

public class ListenerInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        
        if (event.getClickedInventory().getType() != InventoryType.GRINDSTONE) {
            return;
        }

//        if (!(event.getClickedInventory() instanceof GrindstoneInventory)) {
//            return;
//        }

        GrindstoneInventory grindstone = (GrindstoneInventory) event.getClickedInventory();
        ItemStack inputUp = grindstone.getItem(0);
        ItemStack inputDown = grindstone.getItem(1);

    }
}
