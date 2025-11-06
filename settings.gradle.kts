enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://mirrors.tencent.com/nexus/repository/maven-public/")
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
        maven {
            url = uri("https://mirrors.tencent.com/nexus/repository/maven-public/")
        }
    }
}

gradle.projectsLoaded {
    val localFile = File(rootProject.rootDir, "local.properties")
    if (!localFile.exists()) {
        localFile.createNewFile()
    }
}

rootProject.name = "KuiklyMMKV"
include(":androidApp")
include(":shared")
include(":h5App")
include(":miniApp")
include(":mmkvKotlin")