package me.biquaternions.componentcodeofconduct.configuration;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.annotation.PostInject;
import de.bsommerfeld.jshepherd.annotation.Section;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import me.biquaternions.componentcodeofconduct.configuration.types.ButtonConfiguration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Path;
import java.util.List;

@NullMarked
@SuppressWarnings({"NotNullFieldNotInitialized", "FieldMayBeFinal", "unused"})
public class LocaleConfiguration extends ConfigurablePojo<LocaleConfiguration> {

    @SuppressWarnings("NullAway.Init")
    private static LocaleConfiguration FALLBACK;
    public static LocaleConfiguration getFallback() {
        return FALLBACK;
    }

    private static boolean INITIALIZED = false;
    public static void initialize(final Path dataDirectory) {
        if (INITIALIZED) {
            return;
        }

        FALLBACK = ConfigurationLoader.from(dataDirectory.resolve("en_us.yml"))
                .withComments()
                .load(LocaleConfiguration::new);
        FALLBACK.save();
        INITIALIZED = true;
    }

    @Section("info")
    public Info info = new Info();
    public static class Info {

        @Key("version")
        public String version = "1.0";

    }

    @Section("code-of-conduct")
    public CodeOfConduct codeOfConduct = new CodeOfConduct();
    public static class CodeOfConduct {

        @Key("title")
        private String titleString = "<light_purple>Accept our rules!</light_purple>";
        public transient Component title;

        @Key("body")
        private List<String> bodyStrings = List.of("By joining our server you agree that Paper-chan is cute!");
        public transient List<Component> body;

        @Key("timeout")
        public int timeout = 10;

        @Section("agree")
        public ButtonConfiguration agree = new ButtonConfiguration(
                "<color:#edc7ff>Paper-chan is cute!</color>",
                "Click to agree!"
        );

        @Section("disagree")
        public ButtonConfiguration disagree = new ButtonConfiguration(
                "<color:#ff8b8e>I hate Paper-chan!</color>",
                "Click this if you are a bad person!"
        );

    }

    @Section("kick-messages")
    public KickMessages kickMessages = new KickMessages();
    public static class KickMessages {

        @Key("dialog-does-not-exist")
        private String dialogDoesNotExistString = "<red>Unable to join this server at this time</red>";
        public transient Component dialogDoesNotExist;

        @Key("disagree-button-clicked")
        private String disagreeButtonClickedString = "<red>You hate Paper-chan :(</red>";
        public transient Component disagreeButtonClicked;

    }

    @PostInject
    public void convert() {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        this.codeOfConduct.title = miniMessage.deserialize(this.codeOfConduct.titleString);
        this.codeOfConduct.body = this.codeOfConduct.bodyStrings.stream().map(miniMessage::deserialize).toList();
        this.codeOfConduct.agree.convert();
        this.codeOfConduct.disagree.convert();

        this.kickMessages.dialogDoesNotExist = miniMessage.deserialize(this.kickMessages.dialogDoesNotExistString);
        this.kickMessages.disagreeButtonClicked = miniMessage.deserialize(this.kickMessages.disagreeButtonClickedString);
    }

}
