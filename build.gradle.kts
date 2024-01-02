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
    implementation("ai.timefold.solver:timefold-solver-core:1.5.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("ai.timefold.solver:timefold-solver-test:1.5.0")
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