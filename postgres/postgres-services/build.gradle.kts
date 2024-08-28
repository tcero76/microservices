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
    implementation(project(":postgres:postgres-model"))
    implementation(project(":postgres:postgres-config"))
    testImplementation(libs.spring.test)
    testRuntimeOnly(libs.h2)
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.21")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}