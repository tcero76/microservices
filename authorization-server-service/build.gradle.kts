import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    alias(libs.plugins.sk)
    alias(libs.plugins.jvm)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
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
    implementation(libs.spring.security.authorization.server)
    implementation(libs.spring.boot.actuator)
    implementation(libs.kotlin.logging)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.devtools)
    implementation(project(":postgres:postgres-config"))
    implementation(project(":postgres:postgres-model"))
    implementation(project(":postgres:postgres-services"))
    implementation(project(":app-config-data"))
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.2")
}