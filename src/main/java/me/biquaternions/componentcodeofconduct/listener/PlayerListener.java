package me.biquaternions.componentcodeofconduct.listener;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.biquaternions.componentcodeofconduct.concurrent.CodeOfConductFuture;
import me.biquaternions.componentcodeofconduct.misc.CodeOfConductKeys;
import me.biquaternions.componentcodeofconduct.service.CodeOfConductService;
import me.biquaternions.componentcodeofconduct.types.CodeOfConductWrapper;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@NullMarked
public class PlayerListener implements Listener {

    private final ConcurrentMap<UUID, CodeOfConductFuture> awaitingResponses = new ConcurrentHashMap<>();

    @EventHandler
    public void onAsyncPlayerConnectionConfigure(final AsyncPlayerConnectionConfigureEvent event) {
        final PlayerConfigurationConnection connection = event.getConnection();
        final UUID profileId = connection.getProfile().getId();
        if (profileId == null) {
            return;
        }

        final Audience audience = event.getConnection().getAudience();
        final Locale locale = audience.getOrDefault(Identity.LOCALE, Locale.US);
        final Key dialogKey = CodeOfConductKeys.getDialogKey(locale);
        final CodeOfConductWrapper wrapper = CodeOfConductService.getConfigurationForKeyOrFallback(dialogKey);
        if (CodeOfConductService.hasAcceptedCoc(profileId, wrapper)) {
            return;
        }

        final Dialog dialog = RegistryAccess.registryAccess().getRegistry(RegistryKey.DIALOG).get(dialogKey);
        if (dialog == null) {
            audience.closeDialog();
            connection.disconnect(wrapper.configuration().kickMessages.dialogDoesNotExist);
            return;
        }

        final CodeOfConductFuture response = new CodeOfConductFuture(wrapper);
        response.completeOnTimeout(false, wrapper.configuration().codeOfConduct.timeout, TimeUnit.MINUTES);
        this.awaitingResponses.put(profileId, response);
        audience.showDialog(dialog);

        if (!response.join()) {
            audience.closeDialog();
            connection.disconnect(wrapper.configuration().kickMessages.disagreeButtonClicked);
        }

        this.awaitingResponses.remove(profileId);
    }

    @EventHandler
    public void onPlayerCustomClick(final PlayerCustomClickEvent event) {
        if (!(event.getCommonConnection() instanceof PlayerConfigurationConnection connection)) {
            return;
        }

        final UUID profileId = connection.getProfile().getId();
        if (profileId == null) {
            return;
        }

        final Audience audience = connection.getAudience();
        final Locale locale = audience.getOrDefault(Identity.LOCALE, Locale.US);
        final Key buttonAgree = CodeOfConductKeys.getButtonAgreeKey(locale);
        final Key buttonDisagree = CodeOfConductKeys.getButtonDisagreeKey(locale);
        final Key key = event.getIdentifier();
        if (key.equals(buttonAgree)) {
            this.setConnectionAgreement(profileId, true);
        } else if (key.equals(buttonDisagree)) {
            this.setConnectionAgreement(profileId, false);
        }
    }

    @EventHandler
    void onPlayerConnectionCloseEven(final PlayerConnectionCloseEvent event) {
        this.awaitingResponses.remove(event.getPlayerUniqueId());
    }

    private void setConnectionAgreement(final UUID profileId, final boolean value) {
        final CodeOfConductFuture future = this.awaitingResponses.get(profileId);
        if (future != null) {
            future.completeForProfile(profileId, value);
        }
    }

}
