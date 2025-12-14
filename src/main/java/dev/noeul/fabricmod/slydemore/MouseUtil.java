package dev.noeul.fabricmod.slydemore;

import dev.noeul.fabricmod.slydemore.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class MouseUtil {
	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
	private static boolean pointerLocked = false;

	public static void lockPointer() {
		if (!CLIENT.isWindowFocused() || MouseUtil.pointerLocked)
			return;

		if (!MinecraftClient.IS_SYSTEM_MAC)
			KeyBinding.updatePressedStates();

		MouseUtil.pointerLocked = true;
		GLFW.glfwSetInputMode(
				((MinecraftClientAccessor) CLIENT).getWindow().getHandle(),
				GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED
		);
	}

	public static void unlockPointer() {
		if (!MouseUtil.pointerLocked)
			return;

		MouseUtil.pointerLocked = false;
		GLFW.glfwSetInputMode(
				((MinecraftClientAccessor) CLIENT).getWindow().getHandle(),
				GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL
		);
	}
}
