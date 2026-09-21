package me.biquaternions.componentcodeofconduct.util;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NullMarked;

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

}
