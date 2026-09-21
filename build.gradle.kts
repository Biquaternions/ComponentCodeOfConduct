plugins {
    id("java-library")
    id("io.freefair.lombok") version "9.4.0"
    id("com.gradleup.shadow") version "9.6.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    implementation("de.bsommerfeld.jshepherd:core:4.1.1")
    implementation("de.bsommerfeld.jshepherd:yaml:4.1.1")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("26.2")
        jvmArgs("-Xms2G", "-Xmx2G", "-Dcom.mojang.eula.agree=true")
    }

    shadowJar {
        minimize() {
            exclude(dependency("de.bsommerfeld.jshepherd:core"))
            exclude(dependency("de.bsommerfeld.jshepherd:yaml"))
        }
        archiveClassifier.set("")

        mapOf(
            "org.bstats" to "bstats",
            "de.bsommerfeld.jshepherd" to "jshepherd",
            "org.yaml.snakeyaml" to "snakeyaml",
        ).forEach { (key, value) ->
            relocate(key, "me.biquaternions.componentcodeofconduct.libs.$value")
        }

        mergeServiceFiles()
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
