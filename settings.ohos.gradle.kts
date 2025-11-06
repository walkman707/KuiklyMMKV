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

val buildFileName = "build.ohos.gradle.kts"
rootProject.buildFileName = buildFileName

include(":androidApp")
include(":shared")
project(":shared").buildFileName = buildFileName
include(":mmkvKotlin")
//project(":mmkvKotlin").buildFileName = buildFileName  //当前项目mmkvKotlin模块三端共用一套编译脚本。当mmkvKotlin模块需要单独配置build.ohos.gradle.kts编译链时，打开这个注释
