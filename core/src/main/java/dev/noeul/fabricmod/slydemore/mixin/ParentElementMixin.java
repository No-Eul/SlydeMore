package dev.noeul.fabricmod.slydemore.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(ParentElement.class) // <=1.20.6 Compatibility
public interface ParentElementMixin {
	@Inject(method = "mouseReleased", at = @At(value = "HEAD"), cancellable = true)
	default void inject$mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
		// <=1.20.6 Compatibility Check
		int protocolVersion = SharedConstants.getGameVersion().getProtocolVersion();
		if ((protocolVersion & 0x40000000) == 0 // isRelease
				&& protocolVersion > 766 // mcVersion > 1.20.6
				|| protocolVersion >= (0x40000000 | 193) // mcVersion >= 24w18a
		) return;

		if (button == 0 && this.isDragging()) {
			this.setDragging(false);
			if (this.getFocused() != null) {
				callbackInfo.setReturnValue(this.getFocused().mouseReleased(mouseX, mouseY, button));
				return;
			}
		}

		callbackInfo.setReturnValue(this.hoveredElement(mouseX, mouseY)
				.filter(element -> element.mouseReleased(mouseX, mouseY, button))
				.isPresent()
		);
	}

	@Shadow
	boolean isDragging();

	@Shadow
	void setDragging(boolean dragging);

	@Shadow
	@Nullable Element getFocused();

	@Shadow
	Optional<Element> hoveredElement(double mouseX, double mouseY);
}
