plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.noarg") version "1.9.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.optaplanner:optaplanner-core:9.44.0.Final")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}

noArg {
    annotation("com.httpain.constraints.NoArgConstructor")
}