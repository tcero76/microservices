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
val springProfiles:String? = System.getenv("SPRING_PROFILES_ACTIVE")
dependencies {
    api(project(":security"))
    implementation(libs.spring.boot.devtools)
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.actuator)
    implementation(libs.kotlin.logging)
    implementation(libs.logstash.logback.encoder)
    implementation(libs.arrow)
    implementation(project(":postgres:postgres-config"))
    implementation(project(":postgres:postgres-model"))
    implementation(project(":postgres:postgres-services"))
    if(springProfiles.equals("prod")) {
        implementation(libs.spring.cloud.config.client)
        implementation(libs.spring.cloud.discovery.client)
    }
    testImplementation(libs.testcontainer.postgres)
    testImplementation(libs.testcontainer.keycloak)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.security.test)
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}