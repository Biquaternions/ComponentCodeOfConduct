package me.biquaternions.componentcodeofconduct.util;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@NullMarked
@UtilityClass
public class BinaryUtils {

    public final MessageDigest MD;
    public final HexFormat HEX = HexFormat.of();

    static {
        try {
            MD = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte @Nullable [] hashFromPath(final Path path) {
        try {
            return BinaryUtils.MD.digest(Files.readAllBytes(path));
        } catch (IOException exception) {
            return null;
        }
    }

}
