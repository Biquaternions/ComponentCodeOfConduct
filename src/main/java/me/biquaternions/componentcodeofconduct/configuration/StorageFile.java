package me.biquaternions.componentcodeofconduct.configuration;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.annotation.PostInject;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import me.biquaternions.componentcodeofconduct.configuration.types.AcceptedHashConfiguration;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class StorageFile extends ConfigurablePojo<StorageFile> {

    @Key("accepted-hashes")
    public List<AcceptedHashConfiguration> acceptedHashes = List.of();

    @PostInject
    public void convert() {
        this.acceptedHashes.forEach(AcceptedHashConfiguration::convert);
    }

}
