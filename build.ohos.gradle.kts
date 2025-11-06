plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.3.0").apply(false)
    id("com.android.library").version("8.3.0").apply(false)
    kotlin("android").version("2.0.21-KBA-010").apply(false)
    kotlin("multiplatform").version("2.0.21-KBA-010").apply(false)
    id("com.google.devtools.ksp").version("2.0.21-1.0.27").apply(false)

}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://mirrors.tencent.com/nexus/repository/maven-public/")
        }
    }

    dependencies {
        classpath(BuildPlugin.kuikly)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://mirrors.tencent.com/nexus/repository/maven-public/")
        }
    }
}


fun Project.configureKotlinWaring() {
    //关闭kotlin编译警告
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-Xskip-prerelease-check",
                    "-Xabi-stability=unstable",
                    "-Xallow-unstable-dependencies"
                )
            )
        }
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-Xskip-prerelease-check",
                    "-Xabi-stability=unstable",
                    "-Xallow-unstable-dependencies"
                )
            )
        }
    }
}

allprojects {
    afterEvaluate {
        configureKotlinWaring()
    }
}
