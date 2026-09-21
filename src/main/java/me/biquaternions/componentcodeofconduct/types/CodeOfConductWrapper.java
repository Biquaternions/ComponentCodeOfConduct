package me.biquaternions.componentcodeofconduct.types;

import me.biquaternions.componentcodeofconduct.configuration.LocaleConfiguration;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record CodeOfConductWrapper(LocaleConfiguration configuration, byte[] hash) {
}
