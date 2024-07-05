import java.util.function.Consumer
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    alias(libs.plugins.spring.boot)
    id("io.spring.dependency-management") version "1.1.4"
}

val services: List<String> = listOf(
    "resource-service",
    "authorization-server-service",
    "gateway-service",
    "discovery-service",
    "config-server"
)

var versionProject = ""
file("${project.rootDir}/infraestructure/.env").readLines().forEach {
    if(it.substringBefore("=").equals("VERSION")) {
        versionProject = it.substringAfter("=")
    }
}
allprojects {
    repositories {
        mavenCentral()
    }
    tasks {
        withType<BootBuildImage> {
            imageName = "tcero76/${project.name}:${versionProject}"
        }
    }
}

tasks {
    register("buildImageAll") {
        group = "custom"
        services.forEach(Consumer {
            dependsOn(":${it}:bootBuildImage")
            println("Se creó imagen ${it}")
        })
    }
}
