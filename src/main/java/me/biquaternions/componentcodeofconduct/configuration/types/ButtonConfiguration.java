package me.biquaternions.componentcodeofconduct.configuration.types;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.annotation.PostInject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;

@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"NotNullFieldNotInitialized", "FieldMayBeFinal", "unused"})
public class ButtonConfiguration {

    @Key("label")
    private String labelString;
    public transient Component label;

    @Key("tooltip")
    private String tooltipString;
    public transient Component tooltip;

    public ButtonConfiguration(final String labelString, final String tooltipString) {
        this.labelString = labelString;
        this.tooltipString = tooltipString;
    }

    @PostInject
    public void convert() {
        this.label = MiniMessage.miniMessage().deserialize(this.labelString);
        this.tooltip = MiniMessage.miniMessage().deserialize(this.tooltipString);
    }

}
