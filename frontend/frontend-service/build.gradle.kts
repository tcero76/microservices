import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.sk)
    alias(libs.plugins.spring.boot)
//    alias(libs.plugins.flyway)
}

//flyway {
//    url = "jdbc:postgresql://localhost:5432/postgres"
//    user = "root"
//    password = "root"
//    schemas = arrayOf("postgres")
//    locations = arrayOf("filesystem:src/main/resources/db/migration")
//}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.web)
    testImplementation("org.jetbrains.kotlin:kotlin-test")
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