package me.biquaternions.componentcodeofconduct.concurrent;

import lombok.RequiredArgsConstructor;
import me.biquaternions.componentcodeofconduct.configuration.LocaleConfiguration;
import me.biquaternions.componentcodeofconduct.service.CodeOfConductService;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NullMarked
@RequiredArgsConstructor
public class CodeOfConductFuture extends CompletableFuture<Boolean> {

    private final LocaleConfiguration configuration;

    public boolean completeForProfile(final UUID profileId, final boolean value) {
        if (value) {
            CodeOfConductService.updateAcceptedCoc(profileId, this.configuration);
        }
        return this.complete(value);
    }

}
