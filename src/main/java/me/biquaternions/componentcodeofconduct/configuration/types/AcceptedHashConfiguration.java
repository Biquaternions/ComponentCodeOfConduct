package me.biquaternions.componentcodeofconduct.configuration.types;

import de.bsommerfeld.jshepherd.annotation.Key;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import me.biquaternions.componentcodeofconduct.util.BinaryUtils;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class AcceptedHashConfiguration {

    @Key("uuid")
    public UUID uuid;

    @Key("hash")
    private String hashString;
    public transient byte[] hash;

    public AcceptedHashConfiguration(final UUID uuid, final String hashString) {
        this.uuid = uuid;
        this.hashString = hashString;
    }

    public AcceptedHashConfiguration(final UUID uuid, final byte[] hash) {
        this.uuid = uuid;
        this.hashString = BinaryUtils.HEX.formatHex(hash);
    }

    public void convert() {
        this.hash = BinaryUtils.HEX.parseHex(this.hashString);
    }

}
