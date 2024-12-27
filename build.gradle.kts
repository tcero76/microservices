import java.util.function.Consumer
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    alias(libs.plugins.sk)
    alias(libs.plugins.jvm)
    alias(libs.plugins.spring.boot)
    id("io.spring.dependency-management") version "1.1.4"
}
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.vanniktech:gradle-dependency-graph-generator-plugin:0.8.0")
    }
}

allprojects {
    apply(plugin = "com.vanniktech.dependency.graph.generator")
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
    register("_buildImageAll") {
        group = "custom"
        services.forEach(Consumer {
            dependsOn(":${it}:bootBuildImage")
        })
    }
    services.forEach {
        register("build_${it}") {
            group = "custom"
            dependsOn(":${it}:bootBuildImage")
        }
    }
}