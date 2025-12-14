import kotlin.io.path.Path
import kotlin.io.path.exists

plugins {
	// Fabric Loom - https://maven.fabricmc.net/fabric-loom/fabric-loom.gradle.plugin/maven-metadata.xml
	id("java")
	id("fabric-loom") version "1.13.6"
	id("com.dorongold.task-tree") version "4.0.0"
}

repositories {
	mavenCentral()
	maven("https://maven.terraformersmc.com/releases")
	maven("https://maven.shedaniel.me/")
	maven("https://api.modrinth.com/maven")
	maven("https://maven.frohnmeyer-wds.de/artifacts")
}

dependencies {
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

	modCompileOnly("maven.modrinth:sodium:mc1.21.9-0.7.0-fabric")

	// Slyde - https://modrinth.com/mod/slyde/versions
	// Sodium - https://modrinth.com/mod/sodium/versions
	modRuntimeOnly("io.gitlab.jfronny:slyde:1.7.12")
	modRuntimeOnly("maven.modrinth:sodium:mc1.21.10-0.7.3-fabric")

	// Fabric API - // https://fabricmc.net/develop/
	// MixinTrace - // https://modrinth.com/mod/mixintrace/versions
	// Mod Menu - https://modrinth.com/mod/modmenu/versions
	modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:0.134.0+1.21.9")
	modRuntimeOnly("maven.modrinth:mixintrace:1.1.1+1.17")
	modRuntimeOnly("com.terraformersmc:modmenu:16.+")
}

loom {
	sourceSets["main"].resources.files
		.find { file -> file.name.endsWith(".accesswidener") }
		?.let(accessWidenerPath::set)

	runs {
		getByName("client") {
			configName = "Minecraft Client"
			runDir = "run/client"
			ideConfigGenerated(true)
			client()
		}

		getByName("server") {
			configName = "Minecraft Server"
			runDir = "run/server"
			ideConfigGenerated(true)
			server()
		}
	}
}

val targetJavaVersion = JavaVersion.toVersion(property("targetCompatibility")!!)
java {
	targetCompatibility = targetJavaVersion
	sourceCompatibility = targetJavaVersion
	if (JavaVersion.current() < targetJavaVersion)
		toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion.majorVersion.toInt()))
}

tasks {
	processResources {
		inputs.properties(
			"mod_id" to project.property("mod_id"),
			"name" to project.property("mod_name"),
			"version" to project.version,
			"minecraft_version" to project.property("minecraft_version"),
			"loader_version" to project.property("loader_version"),
			"java_version" to project.property("targetCompatibility"),
		)
		filesMatching("fabric.mod.json") {
			expand(inputs.properties)
		}
	}

	compileJava {
		if (targetJavaVersion >= JavaVersion.VERSION_1_10 || JavaVersion.current().isJava10Compatible)
			options.release.set(targetJavaVersion.majorVersion.toInt())
		options.encoding = "UTF-8"
	}

	jar {
		archiveBaseName.set("${project.property("archivesBaseName")}")
		archiveVersion.set("${project.version}+mc${project.property("minecraft_version")}+fabric")

		from("LICENSE.txt")
		exclude(
			"assets/**/*.inkscape.svg",
			"assets/**/*.xcf"
		)
	}

	remapJar {
		archiveBaseName.set("${project.property("archivesBaseName")}")
		archiveVersion.set("${project.version}+mc${project.property("minecraft_version")}+fabric")
	}
}

afterEvaluate {
	loom.runs.configureEach {
		vmArgs(
			"-XX:+IgnoreUnrecognizedVMOptions",
			"-XX:+AllowEnhancedClassRedefinition",
			"-XX:HotswapAgent=fatjar",
			"-Dfabric.development=true",
			"-Dmixin.debug.export=true",
			"-Dmixin.debug.verify=true",
//			"-Dmixin.debug.strict=true",
//			"-Dmixin.debug.countInjections=true",
			"-Dmixin.checks.interfaces=true",
			"-Dfabric.fabric.debug.deobfuscateWithClasspath",
			"-Dmixin.hotSwap=true",
		)

		Path(System.getProperty("java.home"), "lib/hotswap/hotswap-agent.jar")
			.takeIf { it.exists() }
			.let { vmArg("-Dfabric.systemLibraries=$it") }

		configurations["compileClasspath"].incoming
			.artifactView {
				componentFilter {
					it is ModuleComponentIdentifier
							&& it.group == "net.fabricmc"
							&& it.module == "sponge-mixin"
				}
			}
			.files
			.firstOrNull()
			.let { vmArg("-javaagent:$it") }
	}
}
