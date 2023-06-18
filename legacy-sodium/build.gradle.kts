repositories {
	maven("https://api.modrinth.com/maven")
}

dependencies {
	modCompileOnly("maven.modrinth:sodium:mc1.21-0.5.11")

	implementation(project(":core", "namedElements"))
}
