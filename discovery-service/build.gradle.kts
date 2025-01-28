import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


 plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.sk)
    alias(libs.plugins.jvm)
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
    implementation(libs.spring.cloud.discovery.server)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.actuator)
    implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2023.0.2"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}