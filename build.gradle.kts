plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "nl.martenm"
version = "1.26.0"
description = "ServerTutorialPlus"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("org.apache.commons:commons-lang3:3.14.0")
    compileOnly("com.zaxxer:HikariCP:3.4.5")
    compileOnly("com.github.cryptomorin:XSeries:9.5.0")
    implementation("com.github.MartenM:SimpleCommands:1.4.2")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
}

tasks {
    shadowJar {
        relocate("org.bstats", "nl.martenm.libs.bstats")
        relocate("com.cryptomorin.xseries", "nl.martenm.libs.xseries")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}