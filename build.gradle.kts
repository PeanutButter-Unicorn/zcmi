import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.util.Properties
import kotlin.jvm.java

plugins {
    id("it.nicolasfarabegoli.conventional-commits") version "3.1.3"
    id("org.sonarqube") version "latest.release"
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.kotlin.kapt") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.25"
    id("groovy")
    id("io.micronaut.application") version "4.6.1"
    id("com.gradleup.shadow") version "8.3.9"
}

version = project.properties["zcmiVersion"]!!
group = "lol.pbu"

val kotlinVersion = project.properties["kotlinVersion"]!!
val z4jVersion = project.properties["z4jVersion"]!!
val jansiGraalVersion = project.properties["jansiGraalVersion"]!!
val jansiVersion = project.properties["jansiVersion"]!!
val picoJlineVersion = project.properties["picoJlineVersion"]!!
val jlineVersion = project.properties["jlineVersion"]!!

repositories {
    mavenCentral()
}

dependencies {
    kapt("info.picocli:picocli-codegen")
    kapt("io.micronaut.serde:micronaut-serde-processor")
    implementation("info.picocli:picocli")
    implementation("lol.pbu:z4j:${z4jVersion}")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("info.picocli:picocli-jansi-graalvm:${jansiGraalVersion}")
    implementation("org.fusesource.jansi:jansi:${jansiVersion}")
    implementation("info.picocli:picocli-shell-jline3:${picoJlineVersion}")
    implementation("org.jline:jline:${jlineVersion}")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.yaml:snakeyaml")
}

tasks.withType<ProcessResources> {
    val props = Properties()
    file("gradle.properties").inputStream().use { props.load(it) }
    filesMatching("**/application.yml") {
        filter(mapOf("tokens" to props), ReplaceTokens::class.java)
    }
}

application {
    mainClass = "lol.pbu.ZcmiCommand"
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
        showStackTraces = true
        showCauses = true
    }
    finalizedBy(tasks.jacocoTestReport)
}

micronaut {
    testRuntime("spock2")
    processing {
        incremental(true)
        annotations("lol.pbu.*")
    }
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}

graalvmNative.binaries {
    named("main") {
        imageName.set("zcmi")
        buildArgs.add("--color=always")
        buildArgs.add("-march=native")
//        buildArgs.add("-Ob")
    }
}
