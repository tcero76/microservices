import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.sk)
    alias(libs.plugins.jvm)
    alias(libs.plugins.spring.boot)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"

        }
    }
}

dependencies {
    implementation(libs.spring.boot.web)
    implementation(libs.spring.cloud.config.server)
    implementation(libs.spring.boot.security)
    implementation(project(":app-config-data"))
    implementation(libs.logstash.logback.encoder)
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}