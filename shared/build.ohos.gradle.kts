import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("maven-publish")

}

val KEY_PAGE_NAME = "pageName"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        publishLibraryVariants("release")
    }

    ohosArm64 {
        val main by compilations.getting
        compilations.forEach {
            it.kotlinOptions.freeCompilerArgs += when {
                HostManager.hostIsMac -> listOf("-linker-options", "-lmmkv_c_wrapper -L${projectDir}/src/libs/arm64-v8a/")
                else -> throw RuntimeException("暂不支持")
            }
            // 抑制 NativeApi 提示
            it.compilerOptions.options.optIn.addAll(
                "kotlinx.cinterop.ExperimentalForeignApi",
                "kotlin.experimental.ExperimentalNativeApi",
            )
        }
        binaries.all {
            if(debuggable){
//                // 由于libbacktrace性能消耗大，所以只建议在Debug中使用。性能较慢，暂不开启，调试问题时可以开启
//                linkerOpts += "--build-id=sha1"
//                freeCompilerArgs += "-Xadd-light-debug=enable"
//                freeCompilerArgs += "-Xbinary=sourceInfoType=libbacktrace"
            }
            else {
                linkerOpts += "--build-id=sha1"
                freeCompilerArgs += "-Xadd-light-debug=enable"
                freeCompilerArgs += "-Xbinary=sourceInfoType=noop"
            }
        }

        binaries.sharedLib {
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.tencent.kuikly-open:core:${Version.getKuiklyOhosVersion()}")
                implementation("com.tencent.kuikly-open:core-annotations:${Version.getKuiklyOhosVersion()}")
                implementation(projects.mmkvKotlin)  // 源码编译
//                implementation("io.github.walkman707:mmkvKotlin:1.0.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("com.tencent.kuikly-open:core-render-android:${Version.getKuiklyOhosVersion()}")
            }
        }
    }
}

group = "com.kuikly.kuiklymmkv"
version = System.getenv("kuiklyBizVersion") ?: "1.0.0"

publishing {
    repositories {
        maven {
            credentials {
                username = System.getenv("mavenUserName") ?: ""
                password = System.getenv("mavenPassword") ?: ""
            }
            rootProject.properties["mavenUr?"]?.toString()?.let { url = uri(it) }
        }
    }
}

ksp {
    arg(KEY_PAGE_NAME, getPageName())
}

dependencies {
    compileOnly("com.tencent.kuikly-open:core-ksp:${Version.getKuiklyOhosVersion()}") {
        add("kspAndroid", this)
//        add("kspIosArm64", this)
//        add("kspIosX64", this)
//        add("kspIosSimulatorArm64", this)
        add("kspOhosArm64", this)
    }
}

android {
    namespace = "com.kuikly.kuiklymmkv.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
        targetSdk = 30
    }
    sourceSets {
        named("main") {
            jniLibs.srcDirs("src/androidMain/libs/")
            assets.srcDirs("src/commonMain/assets")
        }
    }
}

fun getPageName(): String {
    return (project.properties[KEY_PAGE_NAME] as? String) ?: ""
}

fun getCommonCompilerArgs(): List<String> {
    return listOf(
        "-Xallocator=std"
    )
}

fun getLinkerArgs(): List<String> {
    return listOf()
}

tasks.findByName("linkDebugSharedOhosArm64")?.doFirst {
    copy {
        from("${rootProject.rootDir}/ohosApp/mmkv_c_wrapper/build/default/intermediates/libs/default/arm64-v8a/libmmkv_c_wrapper.so")
        into(projectDir.absolutePath + "/src/libs/arm64-v8a/")
    }

    print("libmmkv_c_wrapper.so copy success\n")
}

// 拷贝debug产物
tasks.findByName("linkDebugSharedOhosArm64")?.doLast {
    copy {
        from(project.buildDir.absolutePath + "/bin/ohosArm64/debugShared/libshared.so")
        into(rootDir.absolutePath + "/ohosApp/entry/libs/arm64-v8a/")
    }
    copy {
        from(project.buildDir.absolutePath + "/bin/ohosArm64/debugShared/libshared_api.h")
        into(rootDir.absolutePath + "/ohosApp/entry/src/main/cpp/")
    }

    print("libkn.so/libkn_api.h copy success\n")
}

// 拷贝release产物
tasks.findByName("linkReleaseSharedOhosArm64")?.doLast {
    copy {
        from(project.buildDir.absolutePath + "/bin/ohosArm64/releaseShared/libshared.so")
        into(rootDir.absolutePath + "/ohosApp/entry/libs/arm64-v8a/")
    }
    copy {
        from(project.buildDir.absolutePath + "/bin/ohosArm64/releaseShared/libshared_api.h")
        into(rootDir.absolutePath + "/ohosApp/entry/src/main/cpp/")
    }

    print("libkn.so and libkn_api.h copy success\n")
}