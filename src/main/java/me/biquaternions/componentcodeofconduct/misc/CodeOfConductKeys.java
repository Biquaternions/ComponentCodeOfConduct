package me.biquaternions.componentcodeofconduct.misc;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

@NullMarked
@UtilityClass
public class CodeOfConductKeys {

    private final String NAMESPACE = "componentcodeofconduct";
    private final String BASE_KEY = "code_of_conduct";

    public Key getDialogKey(final Locale locale) {
        return Key.key(NAMESPACE, String.format("%s/%s/dialog", BASE_KEY, locale.toString().toLowerCase(Locale.ROOT)));
    }

    public Key getButtonAgreeKey(final Locale locale) {
        return Key.key(NAMESPACE, String.format("%s/%s/button/agree", BASE_KEY, locale.toString().toLowerCase(Locale.ROOT)));
    }

    public Key getButtonDisagreeKey(final Locale locale) {
        return Key.key(NAMESPACE, String.format("%s/%s/button/disagree", BASE_KEY, locale.toString().toLowerCase(Locale.ROOT)));
    }


}
