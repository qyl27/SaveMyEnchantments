package cx.rain.mc.savemyenchantments;

import org.bukkit.plugin.java.JavaPlugin;

public final class SaveMyEnchantments extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic


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
    }
}
