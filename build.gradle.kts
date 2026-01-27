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

version = "0.0.1"
group = "lol.pbu"

val kotlinVersion= project.properties["kotlinVersion"]!!
val z4jVersion= project.properties["z4jVersion"]!!

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
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.yaml:snakeyaml")
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

sonarqube {
    properties {
        property("sonar.tests", "src/test/groovy")
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED)
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStackTraces = true
        showCauses = true
    }
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


