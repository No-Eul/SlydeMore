repositories {
	maven("https://api.modrinth.com/maven")
}

dependencies {
	modCompileOnly("maven.modrinth:sodium:mc1.21-0.6.0-beta.1-fabric")

	implementation(project(":core", "namedElements"))
}
