package cx.rain.mc.savemyenchantments.utility;

import de.tr7zw.changeme.nbtapi.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class GrindstoneHelper {
    public static boolean canDisenchant(ItemStack stack) {
        return !stack.getEnchantments().isEmpty();
    }

    public static ItemStack getEnchantBook(Tuple<Enchantment, Integer> enchantment) {
        ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) stack.getItemMeta();
        assert meta != null;
        meta.addStoredEnchant(enchantment.left, enchantment.right, true);
        stack.setItemMeta(meta);
        return stack;
    }

    public static Tuple<ItemStack, ItemStack> disenchant(ItemStack stack) {
        if (canDisenchant(stack)) {
            NBTItem nbt = new NBTItem(stack);
            NBTCompound tag = nbt.getCompound("tag");
            NBTCompoundList enchantments = tag.getCompoundList("Enchantments");

            int size = enchantments.size();
            assert !enchantments.isEmpty();

            NBTListCompound enchantment = enchantments.get(size - 1);
            String id = enchantment.getString("id");
            String[] key = id.split(":");
            int lvl = enchantment.getInteger("lvl");
            ItemStack book = getEnchantBook(new Tuple<>(
                    Enchantment.getByKey(new NamespacedKey(key[0], key[1])), lvl));

            enchantments.remove(size - 1);
            tag.setObject("Enchantments", enchantments);
            nbt.setObject("tag", tag);
            ItemStack item = nbt.getItem();

            return new Tuple<>(item, book);
        } else {
            return null;
        }
    }

    public static ItemStack tryDisenchant(ItemStack stack) {
        Tuple<ItemStack, ItemStack> stackTuple = disenchant(stack);
        if (stackTuple != null) {
            return stackTuple.left;
        }

        return null;
    }

    public static void update(GrindstoneInventory inventory) {
        ItemStack up = inventory.getItem(0);
        ItemStack down = inventory.getItem(1);

        if (up == null
                || down == null) {
            return;
        }

        if (canDisenchant(up)) {
            inventory.setItem(2, tryDisenchant(up));
        }
    }

    public static ItemStack doDisenchantment(GrindstoneInventory inventory) {
        ItemStack up = inventory.getItem(0);
        ItemStack down = inventory.getItem(1);

        if (up == null
                || down == null) {
            return null;
        }

        if (canDisenchant(up)) {
            Tuple<ItemStack, ItemStack> stackTuple = disenchant(up);

            if (stackTuple != null) {
                inventory.setItem(1, stackTuple.right);
                return stackTuple.left;
            }
        }

        return null;
    }
}
