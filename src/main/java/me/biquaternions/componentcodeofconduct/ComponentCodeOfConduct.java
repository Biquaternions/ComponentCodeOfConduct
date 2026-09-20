package me.biquaternions.componentcodeofconduct;

import me.biquaternions.componentcodeofconduct.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ComponentCodeOfConduct extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

}
