package cx.rain.mc.savemyenchantments.utility;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GrindstoneHelper {
    public static boolean canDisenchant(ItemStack stack) {
        return !stack.getEnchantments().isEmpty();
    }

    public static Enchantment disenchant(ItemStack stack) {
        if (canDisenchant(stack)) {
            Map<Enchantment, Integer> enchantments = stack.getEnchantments();

        } else {
            return null;
        }
    }

    public static ItemStack getEnchantBook(ItemStack stack) {
        if (canDisenchant(stack)) {
        }
        return null;
    }
}
