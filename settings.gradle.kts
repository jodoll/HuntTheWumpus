rootProject.name = "HuntTheWumpus"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion: String by settings
        val kspVersion: String by settings

        kotlin("jvm") version kotlinVersion
        id("com.google.devtools.ksp") version "$kotlinVersion-$kspVersion"
    }
}
