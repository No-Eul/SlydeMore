package dev.noeul.fabricmod.slydemore;

public interface GameVersion {
	default int getProtocolVersion() {
		return -1;
	}

	default int protocolVersion() {
		return -1;
	}
}
