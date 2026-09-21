package me.biquaternions.componentcodeofconduct.configuration;

import de.bsommerfeld.jshepherd.annotation.Section;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import me.biquaternions.componentcodeofconduct.configuration.types.AcceptedHashConfiguration;

import java.util.List;

public class StorageFile extends ConfigurablePojo<StorageFile> {

    @Section("accepted-hashes")
    public List<AcceptedHashConfiguration> acceptedHashes = List.of();

}
