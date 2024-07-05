import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    alias(libs.plugins.sk)
    alias(libs.plugins.jvm)
    alias(libs.plugins.spring.boot)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.security.authorization.server)
    implementation(libs.spring.boot.actuator)
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.2")
}