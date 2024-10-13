plugins {
    kotlin("jvm") version "1.9.24"
    id("org.openjfx.javafxplugin") version "0.0.12"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20231013")
    implementation("org.openjfx:javafx-controls:23")
    implementation("org.openjfx:javafx-fxml:23")
    testImplementation(kotlin("test"))
}


application {
    mainClass.set("MainKt")

}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(sourceSets.main.get().output)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}