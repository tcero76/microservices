import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation(libs.spring.boot.jpa)
    implementation(libs.postgres)
    implementation(libs.kotlin.logging)
    implementation(project(":app-config-data"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.21")
}