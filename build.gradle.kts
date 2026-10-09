// The loom-back-compat settings plugin puts Loom on the build script classpath of every project, so each one needs
// the repositories to resolve it from.
buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}

subprojects {
    buildscript {
        repositories {
            mavenCentral()
            gradlePluginPortal()
            maven("https://maven.fabricmc.net/")
        }
    }
}
