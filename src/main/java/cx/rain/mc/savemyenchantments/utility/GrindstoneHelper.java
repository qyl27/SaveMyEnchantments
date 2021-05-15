package cx.rain.mc.savemyenchantments.utility;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class GrindstoneHelper {
    public static boolean canDisenchant(ItemStack stack) {
        return !stack.getEnchantments().isEmpty();
    }

    public static ItemStack getEnchantBook(Enchantment enchantment, int lvl) {
        ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) stack.getItemMeta();
        assert meta != null;
        meta.addStoredEnchant(enchantment, lvl, true);
        stack.setItemMeta(meta);
        return stack;
    }

    public static Tuple<ItemStack, ItemStack> disenchant(ItemStack stack) {
        if (canDisenchant(stack)) {
            net.minecraft.server.v1_16_R3.ItemStack stackNms = CraftItemStack.asNMSCopy(stack);
            NBTTagCompound nbt = stackNms.getTag();
            NBTTagList enchantments = nbt.getList("Enchantments", 10);

            int size = enchantments.size();
            assert !enchantments.isEmpty();

            NBTTagCompound enchantment = enchantments.getCompound(size - 1);
            String id = enchantment.getString("id");
            String[] keys = id.split(":");
            int lvl = enchantment.getInt("lvl");

            ItemStack book = getEnchantBook(Enchantment.getByKey(new NamespacedKey(keys[0], keys[1])), lvl);
            enchantments.remove(size - 1);

            if (enchantments.size() > 0) {
                nbt.setInt("RepairCost", nbt.getInt("RepairCost") - 1);
                nbt.set("Enchantments", enchantments);
            } else {
                nbt.remove("RepairCost");
                nbt.remove("Enchantments");
            }
            stackNms.setTag(nbt);

            ItemStack item = CraftItemStack.asBukkitCopy(stackNms);

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

    public static Tuple<ItemStack, ItemStack> doDisenchantment(GrindstoneInventory inventory) {
        ItemStack up = inventory.getItem(0);
        ItemStack down = inventory.getItem(1);

        if (up == null
                || down == null) {
            return null;
        }

        if (canDisenchant(up)) {
            return disenchant(up);
        }

        return null;
    }
}
