package me.biquaternions.componentcodeofconduct.configuration.types;

import de.bsommerfeld.jshepherd.annotation.Key;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.biquaternions.componentcodeofconduct.util.BinaryUtils;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"NotNullFieldNotInitialized", "FieldMayBeFinal", "unused"})
public class AcceptedHashConfiguration {

    @Key("uuid")
    private String uuidString;
    public transient UUID uuid;

    @Key("hash")
    private String hashString;
    public transient byte[] hash;

    public AcceptedHashConfiguration(final UUID uuid, final String hashString) {
        this.uuidString = uuid.toString();
        this.hashString = hashString;
        this.convert();
    }

    public AcceptedHashConfiguration(final UUID uuid, final byte[] hash) {
        this.uuidString = uuid.toString();
        this.hashString = BinaryUtils.HEX.formatHex(hash);
        this.convert();
    }

    public void convert() {
        this.uuid = UUID.fromString(this.uuidString);
        this.hash = BinaryUtils.HEX.parseHex(this.hashString);
    }

}
