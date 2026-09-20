package me.biquaternions.componentcodeofconduct.service;

import lombok.experimental.UtilityClass;
import me.biquaternions.componentcodeofconduct.configuration.LocaleConfiguration;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@NullMarked
@UtilityClass
public class CodeOfConductService {

    private final ConcurrentMap<Key, LocaleConfiguration> CONFIGURATIONS = new ConcurrentHashMap<>();

    public void putConfigurationForKey(final Key key, final LocaleConfiguration configuration) {
        CONFIGURATIONS.put(key, configuration);
    }

    public LocaleConfiguration getConfigurationForKeyOrFallback(final Key key) {
        return CONFIGURATIONS.getOrDefault(key, LocaleConfiguration.getFallback());
    }

}
