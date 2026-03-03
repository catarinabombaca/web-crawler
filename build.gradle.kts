import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass = "com.crawler.WebCrawlerKt"
}

repositories {
    mavenCentral()
}

tasks {
    withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            allWarningsAsErrors.set(false)
            jvmTarget.set(JVM_21)
            freeCompilerArgs.add("-jvm-default=enable")
        }
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        outputs.upToDateWhen { false }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {

    implementation(platform("org.http4k:http4k-bom:6.31.0.0"))
    implementation("org.http4k:http4k-client-okhttp")
    implementation("org.http4k:http4k-core")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("dev.forkhandles:result4k:2.22.3.0")
    testImplementation("org.http4k:http4k-server-undertow")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.junit.platform:junit-platform-launcher:1.10.2")
    testImplementation("io.mockk:mockk:1.14.9")
}

