import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "jp.iceserver"
version = "2.3.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-snapshots/")

    /*
     * item  : PaperMC
     * usage : Base
     */
    maven("https://papermc.io/repo/repository/maven-public/")


    /*
     * item  : Opencollab Repository
     * usage : FloodgateApi
     */
    maven("https://repo.opencollab.dev/main/")
}

dependencies {
    implementation(kotlin("stdlib"))

    /*
     * item  : MySQL
     * usage : DataStorage
     */
    val exposedVersion = "0.39.2"
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("mysql:mysql-connector-java:8.0.33")

    /*
     * item  : DiscordWebhook
     * usage : /report Transaction
     */
    implementation("club.minnced", "discord-webhooks", "0.8.4")

    /*
     * item  : Ktor,    Thumbnailator, Klaxon
     * usage : FaceApi, ImageResize  , JsonParser
     */
    implementation("io.ktor", "ktor-server-core-jvm", "2.3.6")
    implementation("io.ktor", "ktor-server-netty-jvm", "2.3.6")
    implementation("net.coobird", "thumbnailator", "0.4.14")
    implementation("com.beust", "klaxon", "5.5")

    implementation("net.wesjd", "anvilgui", "1.5.3-SNAPSHOT")
    implementation("com.github.M1n1don", "SmartInvsR", "2.0.0")
    implementation("com.github.hazae41", "mc-kutils", "master-SNAPSHOT")

    /*
     * item  : LuckPermsApi
     * usage : /staffPermissionModify
     */
    compileOnly("net.luckperms:api:5.4")

    /*
     * item  : PaperMCApi
     * usage : Base
     */
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    /*
     * item  : FloodgateApi
     * uasge : FaceApi
     */
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveBaseName.set("IceCat")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")

        mergeServiceFiles()

        manifest {
            attributes(mapOf("Main-Class" to "jp.iceserver.icecat.IceCatKt"))
        }
    }

    processResources {
        filteringCharset = "UTF-8"
        from(sourceSets["main"].resources.srcDirs) {
            include("**/*.yml")
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
            filter<ReplaceTokens>("tokens" to mapOf("name" to "IceCat"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}