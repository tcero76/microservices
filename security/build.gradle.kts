import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.sk)
    alias(libs.plugins.jvm)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }
}

dependencies {
    implementation(libs.spring.boot.oauth2.server)
    implementation(libs.kotlin.logging)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.spring.test)
}