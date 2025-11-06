buildscript {
    dependencies {
        classpath(BuildPlugin.kuikly)
    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    // mmkv:2.1.0版本开始，升级了对androidx.annotation的依赖版本到1.9.0.这会触发androidx 高版本的兼容 bug。引发编译错误，需要升级AGP到8.3.0+
    id("com.android.application").version("8.3.0").apply(false)
    id("com.android.library").version("8.3.0").apply(false)
    kotlin("android").version("2.0.21-KBA-010").apply(false)  // 为了mmkvKotlin模块使用统一脚本，这里配置了适配ohos的Kotlin编译工具链
    kotlin("multiplatform").version("2.0.21-KBA-010").apply(false)  // 为了mmkvKotlin模块使用统一脚本，这里配置了适配ohos的Kotlin编译工具链
    id("com.google.devtools.ksp").version("2.1.21-2.0.1").apply(false)

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
