rootProject.name = "slyde-more"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
	}
}

include("core")
include("sodium")
include("legacy-sodium")
