package me.biquaternions.componentcodeofconduct;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import de.bsommerfeld.jshepherd.core.PersistenceDelegateFactoryRegistry;
import de.bsommerfeld.jshepherd.yaml.YamlPersistenceDelegateFactory;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
import me.biquaternions.componentcodeofconduct.configuration.LocaleConfiguration;
import me.biquaternions.componentcodeofconduct.misc.CodeOfConductKeys;
import me.biquaternions.componentcodeofconduct.service.CodeOfConductService;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.Translator;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Stream;

@NullMarked
@SuppressWarnings({"UnstableApiUsage", "unused"})
class ComponentCodeOfConductBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(final BootstrapContext context) {

        PersistenceDelegateFactoryRegistry.registerFactory(new YamlPersistenceDelegateFactory());
        context.getLifecycleManager().registerEventHandler(RegistryEvents.DIALOG.compose(), event -> {

            try {
                Path dataDirectory = context.getDataDirectory();
                Files.createDirectories(dataDirectory);
                LocaleConfiguration.initialize(dataDirectory);

                try (final Stream<Path> paths = Files.list(context.getDataDirectory())) {

                    paths.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".yml"))
                            .forEach(path -> {

                                final String pathString = path.getFileName().toString();
                                final String localeString = StringUtils.removeEnd(pathString, ".yml");
                                final Locale locale = Translator.parseLocale(localeString);
                                if (locale == null) {
                                    return;
                                }

                                final LocaleConfiguration config = ConfigurationLoader.from(path)
                                        .withComments()
                                        .load(LocaleConfiguration::new);
                                config.save();

                                Key dialogKey = CodeOfConductKeys.getDialogKey(locale);
                                event.registry().register(DialogKeys.create(dialogKey), builder -> {
                                    // From the paper docs :o
                                    builder.base(DialogBase.builder(config.codeOfConduct.title)
                                                    .canCloseWithEscape(false)
                                                    .body(config.codeOfConduct.body.stream().map(DialogBody::plainMessage).toList())
                                                    .build()
                                            ).type(
                                                    DialogType.confirmation(
                                                            ActionButton.builder(config.codeOfConduct.disagree.label)
                                                                    .tooltip(config.codeOfConduct.disagree.tooltip)
                                                                    .action(DialogAction.customClick(CodeOfConductKeys.getButtonDisagreeKey(locale), null))
                                                                    .build(),
                                                            ActionButton.builder(config.codeOfConduct.agree.label)
                                                                    .tooltip(config.codeOfConduct.agree.tooltip)
                                                                    .action(DialogAction.customClick(CodeOfConductKeys.getButtonAgreeKey(locale), null))
                                                                    .build()
                                            ));
                                });
                                CodeOfConductService.putConfigurationForKey(dialogKey, config);

                            });

                }

            } catch (IOException exception) {
                context.getLogger().error("Failed to read code of conducts", exception);
            }

        });
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return PluginBootstrap.super.createPlugin(context);
    }

}
