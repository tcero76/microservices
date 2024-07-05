import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.sk)
    alias(libs.plugins.spring.boot)
    id("io.spring.dependency-management") version "1.1.4"
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

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.2")
    }
}
dependencies {
    implementation(libs.gateway)
    implementation(libs.spring.cloud.discovery.client)
    implementation(libs.kotlin.logging)
    implementation(libs.spring.boot.actuator)
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.2")
    implementation(project(":app-config-data"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}