package dev.noeul.fabricmod.slydemore.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GameVersion;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(GameVersion.class)
public interface GameVersionMixin extends dev.noeul.fabricmod.slydemore.GameVersion {
}
