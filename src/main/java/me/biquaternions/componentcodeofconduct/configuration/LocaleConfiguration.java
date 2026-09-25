package me.biquaternions.componentcodeofconduct.configuration;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.annotation.PostInject;
import de.bsommerfeld.jshepherd.annotation.Section;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import me.biquaternions.componentcodeofconduct.configuration.types.ButtonConfiguration;
import me.biquaternions.componentcodeofconduct.util.BinaryUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

@NullMarked
@SuppressWarnings({"NotNullFieldNotInitialized", "FieldMayBeFinal", "unused"})
public class LocaleConfiguration extends ConfigurablePojo<LocaleConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleConfiguration.class);

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

        final Path path = dataDirectory.resolve("en_us.yml");
        final LocaleConfiguration configuration = ConfigurationLoader.from(path)
                .withComments()
                .load(LocaleConfiguration::new);
        configuration.save();

        FALLBACK = configuration;
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
        public transient byte[] hash;

        @Key("timeout")
        public int timeout = 10;

        @Key("width")
        private int internalWidth = 200;
        public transient int width;

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
        this.codeOfConduct.hash = BinaryUtils.MD.digest(String.join("\n", this.codeOfConduct.bodyStrings).getBytes(StandardCharsets.UTF_8));
        this.codeOfConduct.agree.convert();
        this.codeOfConduct.disagree.convert();

        this.kickMessages.dialogDoesNotExist = miniMessage.deserialize(this.kickMessages.dialogDoesNotExistString);
        this.kickMessages.disagreeButtonClicked = miniMessage.deserialize(this.kickMessages.disagreeButtonClickedString);
    }

    @PostInject
    public void validate() {
        this.codeOfConduct.width = Math.clamp(this.codeOfConduct.internalWidth, 1, 1024);
        if (this.codeOfConduct.internalWidth < 1 || this.codeOfConduct.internalWidth > 1024) {
            LOGGER.warn("Width is expected to be between 1 and 1024");
        }
    }

}
