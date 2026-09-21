package me.biquaternions.componentcodeofconduct;

import lombok.RequiredArgsConstructor;
import me.biquaternions.componentcodeofconduct.configuration.StorageFile;
import me.biquaternions.componentcodeofconduct.listener.PlayerListener;
import me.biquaternions.componentcodeofconduct.service.CodeOfConductService;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public final class ComponentCodeOfConduct extends JavaPlugin {

    private final StorageFile storage;

    @Override
    public void onLoad() {
        CodeOfConductService.populateAcceptedCoC(this.storage);
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        this.storage.acceptedHashes = CodeOfConductService.dumpAcceptedCoC();
        this.storage.save();
    }

}
