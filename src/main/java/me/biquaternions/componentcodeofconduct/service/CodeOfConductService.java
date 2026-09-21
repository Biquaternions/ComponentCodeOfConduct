package me.biquaternions.componentcodeofconduct.service;

import lombok.experimental.UtilityClass;
import me.biquaternions.componentcodeofconduct.configuration.LocaleConfiguration;
import me.biquaternions.componentcodeofconduct.configuration.StorageFile;
import me.biquaternions.componentcodeofconduct.configuration.types.AcceptedHashConfiguration;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@NullMarked
@UtilityClass
public class CodeOfConductService {

    private final ConcurrentMap<Key, LocaleConfiguration> CONFIGURATIONS = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, byte[]> ACCEPTED_CODE_OF_CONDUCTS = new ConcurrentHashMap<>();

    public void putConfigurationForKey(final Key key, final LocaleConfiguration configuration) {
        CONFIGURATIONS.put(key, configuration);
    }

    public LocaleConfiguration getConfigurationForKeyOrFallback(final Key key) {
        return CONFIGURATIONS.getOrDefault(key, LocaleConfiguration.getFallback());
    }

    public void populateAcceptedCoC(final StorageFile file) {
        ACCEPTED_CODE_OF_CONDUCTS.putAll(
                file.acceptedHashes.stream()
                        .collect(Collectors.toMap(e -> e.uuid, e -> e.hash))
        );
    }

    public List<AcceptedHashConfiguration> dumpAcceptedCoC() {
        return ACCEPTED_CODE_OF_CONDUCTS.entrySet()
                .stream()
                .map(entry -> new AcceptedHashConfiguration(entry.getKey(), entry.getValue()))
                .toList();
    }

    public boolean hasAcceptedCoc(final UUID uuid, final LocaleConfiguration configuration) {
        final byte[] hash = ACCEPTED_CODE_OF_CONDUCTS.get(uuid);
        if (hash == null) {
            return false;
        }
        return Arrays.equals(hash, configuration.codeOfConduct.hash);
    }

    public void updateAcceptedCoc(final UUID uuid, final LocaleConfiguration configuration) {
        ACCEPTED_CODE_OF_CONDUCTS.put(uuid, configuration.codeOfConduct.hash);
    }

}
