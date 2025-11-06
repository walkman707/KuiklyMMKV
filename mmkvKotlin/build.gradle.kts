import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("maven-publish")
    signing
}

version = MavenConfig.VERSION
group = MavenConfig.GROUP

publishing {
    repositories {
        val username = MavenConfig.getUsername(project)
        val password = MavenConfig.getPassword(project)
        if (username.isNotEmpty() && password.isNotEmpty()) {
            maven {
                credentials {
                    setUsername(username)
                    setPassword(password)
                }
                url = uri(MavenConfig.getRepoUrl(version as String))
            }
        } else {
            mavenLocal()
//            // 发布到build/repo目录
//            maven {
//                url = uri(layout.buildDirectory.dir("repo"))
//            }
        }

        publications.withType<MavenPublication>().configureEach {
            pom.configureMavenCentralMetadata()
            signPublicationIfKeyPresent(project)
        }
    }
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    KotlinHierarchyTemplate.default

    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        publishLibraryVariants("release")
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()

    cocoapods {
//        HtmlStyle.summary = "Some description for the Shared Module"
        version = "1.0"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "18.4"
        osx.deploymentTarget = "15.4"
        framework {
            baseName = "MMKV-Kotlin"
            isStatic = true
        }
        pod("MMKV", "1.2.14")
    }
// 统一编译脚本，配置鸿蒙target
    ohosArm64 {
        val main by compilations.getting

//        val devopsFlag = providers.environmentVariable("PIPELINE_ID").orNull != null
        val devopsFlag = true

        val includeDir = "${rootProject.rootDir}/ohosApp/mmkv_c_wrapper/src/main/cpp/include/"
        val soLibDir = if(devopsFlag) {
            "${rootProject.rootDir}/shared/src/libs/arm64-v8a"
        } else {
            "${rootProject.rootDir}/ohosApp/mmkv_c_wrapper/build/default/intermediates/libs/default/arm64-v8a"
        }
        // 需要在mmkvKotlin里调来自外部C的时候加这个
        val interop by main.cinterops.creating {
            definitionFile = file("src/nativeInterop/cinterop/interop.def")
            includeDirs(includeDir)
            extraOpts("-libraryPath", soLibDir)
        }
        compilations.forEach {
//            it.kotlinOptions.freeCompilerArgs += when {
//                HostManager.hostIsMac -> listOf("-linker-options", "-lmmkv_c_wrapper -L${soLibDir}")
//                else -> throw RuntimeException("暂不支持")
//            }
            // 抑制 NativeApi 提示
            it.compilerOptions.options.optIn.addAll(
                "kotlinx.cinterop.ExperimentalForeignApi",
                "kotlin.experimental.ExperimentalNativeApi",
            )
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.mmkv)
        }
        androidInstrumentedTest.dependencies {

        }
        // 统一编译脚本，配置鸿蒙target
        val ohosArm64Main by getting {
            dependencies {
//                implementation(libs.knoi)

            }
        }
    }
}

android {
    namespace = "com.kuikly.thirdparty.kmp.lib.mmkv"
    compileSdk = 34
    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
