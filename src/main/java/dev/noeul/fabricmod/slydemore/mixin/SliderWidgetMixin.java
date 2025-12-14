package dev.noeul.fabricmod.slydemore.mixin;

import dev.noeul.fabricmod.slydemore.MouseUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11909;
import net.minecraft.client.gui.widget.SliderWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SliderWidget.class)
public class SliderWidgetMixin {
	@Inject(method = "onClick", at = @At("HEAD"))
	private void inject$onClick(class_11909 click, boolean bl, CallbackInfo callbackInfo) {
		MouseUtil.lockPointer();
	}

	@Inject(method = "onRelease", at = @At("HEAD"))
	private void inject$onRelease(class_11909 click, CallbackInfo callbackInfo) {
		MouseUtil.unlockPointer();
	}
}
