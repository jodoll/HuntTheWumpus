rootProject.name = "HuntTheWumpus"

pluginManagement {
    plugins {
        val kotlinVersion : String by settings

        kotlin("jvm") version kotlinVersion
    }
}
