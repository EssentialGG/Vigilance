package gg.essential.vigilance.impl;

import net.minecraft.client.resources.I18n;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
@SuppressWarnings("unused") // instantiated via reflection from Platform.Companion
public class PlatformImpl implements Platform {

    @NotNull
    @Override
    public String i18n(@NotNull String key) {
        return I18n.format(key);
    }
}
