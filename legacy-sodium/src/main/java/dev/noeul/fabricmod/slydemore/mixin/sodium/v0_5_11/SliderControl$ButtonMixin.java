package dev.noeul.fabricmod.slydemore.mixin.sodium.v0_5_11;

import dev.noeul.fabricmod.slydemore.MouseUtil;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(targets = "me.jellysquid.mods.sodium.client.gui.options.control.SliderControl$Button")
public class SliderControl$ButtonMixin extends ControlElement<Integer> {
	public SliderControl$ButtonMixin(Option<Integer> option, Dim2i dim) {
		super(option, dim);
	}

	@Inject(method = "mouseClicked", at = @At(
			value = "INVOKE",
			target = "Lme/jellysquid/mods/sodium/client/gui/options/control/SliderControl$Button;setValueFromMouse(D)V"
	))
	private void inject$mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
		MouseUtil.lockPointer();
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		MouseUtil.unlockPointer();
		return true;
	}
}
