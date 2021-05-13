package cx.rain.mc.savemyenchantments;

import cx.rain.mc.savemyenchantments.listener.ListenerInventoryClick;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaveMyEnchantments extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getLogger().info("Loading.");
        Bukkit.getPluginManager().registerEvents(new ListenerInventoryClick(), this);

        try {
            Class.forName("io.izzel.arclight.common.mod.ArclightMod");
            getLogger().info("Hi Arclight, did you hear about limelight?");
        } catch (ClassNotFoundException ignored) {
            // Silence is gold.
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().info("Stopping.");
    }
}
