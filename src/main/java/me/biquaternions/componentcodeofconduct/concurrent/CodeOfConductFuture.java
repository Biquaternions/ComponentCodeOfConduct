package me.biquaternions.componentcodeofconduct.concurrent;

import lombok.RequiredArgsConstructor;
import me.biquaternions.componentcodeofconduct.service.CodeOfConductService;
import me.biquaternions.componentcodeofconduct.types.CodeOfConductWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NullMarked
@RequiredArgsConstructor
public class CodeOfConductFuture extends CompletableFuture<Boolean> {

    private final CodeOfConductWrapper wrapper;

    public boolean completeForProfile(final UUID profileId, final boolean value) {
        if (value) {
            CodeOfConductService.updateAcceptedCoc(profileId, this.wrapper);
        }
        return this.complete(value);
    }

}
