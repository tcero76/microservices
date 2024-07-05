import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.sk)
    alias(libs.plugins.jvm)
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
//dependencyManagement {
//    imports {
//        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.2")
//    }
//}
dependencies {
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.cloud.config.client)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.cloud.discovery.client)
    implementation(libs.kotlin.logging)
    implementation(libs.spring.boot.oauth2.server)
    implementation(libs.postgres)
    implementation(libs.logstash.logback.encoder)
    implementation(project(":postgres:postgres-config"))
    implementation(project(":postgres:postgres-model"))
    implementation(project(":postgres:postgres-services"))
    testImplementation(libs.testcontainer.postgres)
    testImplementation(libs.testcontainer.keycloak)
    testImplementation(libs.spring.test)
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}