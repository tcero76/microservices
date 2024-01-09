import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.sk)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.flyway)
    id("com.palantir.docker") version "0.35.0"
//    id("com.palantir.docker-run") version "0.35.0"
}
docker {
    name = "tcero76/app:".plus(version)
//    uri("tcero76/app".plus(version))
//    tag("name", "app")
    copySpec.from("build").into("build")
//    push(true)
    setDockerfile(file("Dockerfile"))
}

repositories {
    mavenCentral()
}

flyway {
    url = "jdbc:postgresql://localhost:5432/postgres"
    user = "root"
    password = "root"
    schemas = arrayOf("postgres")
    locations = arrayOf("filesystem:src/main/resources/db/migration")
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
    implementation(libs.spring.boot)
    implementation(libs.kotlin.reflect)
    implementation(libs.spring.docker)
    implementation(libs.flyway.publish)
    implementation(libs.spring.boot.jpa)
    implementation(libs.postgres)
}
