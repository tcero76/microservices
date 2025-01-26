import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.oauth2.client)
    implementation(libs.spring.boot.webflux)
    implementation(libs.spring.boot.validation)
    implementation(libs.kotlin.logging)
    implementation(libs.spring.cloud.config.client)
    implementation(libs.spring.cloud.discovery.client)
    implementation(project(":app-config-data"))
    implementation(project(":resource-service-common"))
}