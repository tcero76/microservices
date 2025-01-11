import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.sk)
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

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.2")
    }
}
val springProfiles:String? = System.getenv("SPRING_PROFILES_ACTIVE")
dependencies {
    if(springProfiles.equals("prod")) {
        implementation(libs.spring.cloud.discovery.client)
        implementation(libs.spring.cloud.config.client)
    }
    implementation(libs.gateway)
    implementation(libs.kotlin.logging)
    implementation(libs.spring.boot.actuator)
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.2")
    implementation(project(":app-config-data"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}