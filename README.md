# mmkvKotlin
mmkvKotlin是基于 Kotlin MultiPlatform 技术，对 腾讯开源组件 [MMKV](https://github.com/Tencent/MMKV) 的跨端封装，适用于基于 KMP 平台的跨端工程。

支持平台：
- Android
- iOS
- ohosArm64（鸿蒙）

## 1.简介
本项目以携程开源的 [mmkv-kotlin](https://github.com/ctripcorp/mmkv-kotlin) 为基础，新增了对鸿蒙平台的适配，支持在跨端层使用Kotlin语言进行多平台kv存储读写。

## 2.快速接入及代码示例
### 2.1 跨端侧
1. 指定仓库

   mavenCentral()
2. 添加mmkvKotlin依赖

   参考shared模块： 在shared/build.gradle.kts中增加Android/iOS构建目标的commonMain对mmkvKotlin依赖
```kotlin
...
val commonMain by getting {
   dependencies {
      implementation("com.tencent.kuikly-open:core:${Version.getKuiklyVersion()}")
      implementation("com.tencent.kuikly-open:core-annotations:${Version.getKuiklyVersion()}")
      implementation("io.github.walkman707:mmkvKotlin:1.0.1")
   }
}
...
```

   在shared/build.ohos.gradle.kts中增加Ohos构建目标的commonMain对mmkvKotlin依赖
```kotlin
...
val commonMain by getting {
   dependencies {
      implementation("com.tencent.kuikly-open:core:${Version.getKuiklyOhosVersion()}")
      implementation("com.tencent.kuikly-open:core-annotations:${Version.getKuiklyOhosVersion()}")
      implementation("io.github.walkman707:mmkvKotlin:1.0.1")
   }
}
...
```
:::tip 注意

鸿蒙额外配置：
- 拷贝本项目shared/src/libs/arm64-v8a/libmmkv_c_wrapper.so文件到你工程的shared模块路径下:shared/libs/
- 在你的shared模块shared/build.ohos.gradle.kts 增加``-lmmkv_c_wrapper`编译选项，参考shared/build.ohos.gradle.kts配置。这一步很重要，否则会出现链接找不到符号错误。
```script
...
ohosArm64 {
        val main by compilations.getting
        compilations.forEach {
            it.kotlinOptions.freeCompilerArgs += when {
                HostManager.hostIsMac -> listOf("-linker-options", "-lmmkv_c_wrapper -L${projectDir}/src/libs/arm64-v8a/")
                else -> throw RuntimeException("暂不支持")
            }
        }
        
    }
...
```
:::

3. 测试代码示例
参考MMKVKotlinTest.kt类


### 2.2 安卓侧
1. 指定仓库：

   mavenCentral()
2. 添加依赖

```kotlin
dependencies {
   implementation(libs.mmkv)
}
```

3. 初始化MMKV
参考androidAPP示例代码，在KRApplication中初始化MMKV实例
```kotlin
    override fun onCreate() {
   super.onCreate()

   MMKV.initialize(this)

   MMKV.defaultMMKV().run {
      encode("test", "abcdef")
   }
}
```

### 2.3 iOS侧
1. 添加依赖
```script
  pod 'MMKV', '1.2.14'
```
2. 初始化MMKV
   参考iOSAPP示例代码，在iOSApp.swift中初始化MMKV实例
```swift
    init() {
        // 初始化MMKV
        initializeMMKV()
        }
    private func initializeMMKV() {
        // 设置MMKV根目录
        let rootDir = MMKV.initialize(rootDir: nil)
        print("MMKV root directory: \(rootDir)")
        
        // 获取默认MMKV实例
        guard let mmkv = MMKV.default() else {
            print("Failed to initialize MMKV")
            return
        }
        
        mmkv.set("abcdef", forKey: "test")
        
        print("MMKV initialized successfully")
        
        // 可选：设置加密密钥（用于敏感数据）
        // mmkv.setEncryptKey("your_encryption_key".data(using: .utf8))
    }
```

### 2.4 Ohos侧
1. 添加依赖

   在需要entry模块，添加
```script
   "@tencent/mmkv": "~2.2.1"
```

2. 初始化MMKV
   参考ohosApp示例代码，在AbilityState.ets中初始化MMKV实例
```ts
export default class AbilityState extends AbilityStage {
  onCreate(): void {
    super.onCreate()
    // Napi.initKuikly();

    //直接调用鸿蒙版 MMKV 初始化，和 kmp 共用同一个
    MMKV.initialize(this.context.getApplicationContext())

    MMKV.defaultMMKV().encodeString("test", "abcdef")
    hilog.info(0x0000, 'mmkv', MMKV.defaultMMKV().decodeString("test"))
  }
}
```

## 3.开发指引
### 工程说明
1. 本工程通过[Kuikly模板工程](https://kuikly.tds.qq.com/DevGuide/as-plugin.html)创建。在模板工程基础上，增加mmkvKotlin Multi-Platform模块，实现对[MMKV](https://github.com/Tencent/MMKV)组件的KMP封装。
2. 本工程mmkvKotlin模块，采用Android/iOS/Ohos统一编译链方案。即，Android/iOS/Ohos target，均通过kLottiView/build.gradle.kts配置。收益是统一编译工具，配置更简洁，同时ohos编译脚本是默认设置，ohosArm64Main中的代码也有相应编译器高亮提示。
3. 如果业务多端对Kotlin版本要求不一致问题，也可以修改mmkvKotlin模块，采用Ohos单独配置编译链方案。即，Android/iOS制品编译链，通过kLottiView/build.gradle.kts构建，Ohos制品通过kLottieView/build.ohos.gradle.kts构建。

### 环境配置
1. AGP版本

   mmkv:2.1.0版本开始，升级了对androidx.annotation的依赖版本到1.9.0。这会触发androidx 高版本的兼容 bug，引发编译错误，需要升级AGP到8.3.0+
   如果业务AGP版本要求低于8.3.0，则可以修改本组件，依赖低版本mmkv组件。

2. JDK版本
   升级AGP到8.3.0+后，则JDK也需要升级到17。

   参考本工程，可在gradle.properties中指定gradle java home版本：

   org.gradle.java.home=/Library/Java/JavaVirtualMachines/jdk-17.0.15.jdk/Contents/Home


### 工程目录结构
```shell
.
├── README.md
├── androidApp # mmkvKotlin 使用示例
├── build.gradle.kts # Android/iOS根构建脚本
├── build.ohos.gradle.kts # Ohos根构建脚本
├── buildSrc
├── gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
├── h5App
├── iosApp # mmkvKotlin 使用示例
├── kotlin-js-store
├── local.properties
├── miniApp
├── mmkvKotlin # mmkvKotlin KMP工程
├── ohosApp # mmkvKotlin 使用示例
├── package.json
├── settings.gradle.kts
├── settings.ohos.gradle.kts
└── shared # Kuikly跨端侧使用mmkvKotlin Demo示例


```
### mmkvKotlin 鸿蒙适配
mmkvKotlin 模块鸿蒙基于cinterop，使用Kotlin语法与MMKV native so(libmmkv.so)进行交互。
1. MMKV C接口封装

   在ohosAPP/mmkv-c-wrapper模块，提供了对libmmkv.so的C接口封装源码。
2. 鸿蒙构建脚本配置

   在mmkvKotlin/build.gradle.kts(统一Kotlin编译工具链)或mmkvKotlin/build.ohos.gradle.kts(鸿蒙独立Kotlin编译工具链)中，进行cinterop配置，指定C头文件和SO路径：
```script
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
            it.kotlinOptions.freeCompilerArgs += when {
                HostManager.hostIsMac -> listOf("-linker-options", "-lmmkv_c_wrapper -L${soLibDir}")
                else -> throw RuntimeException("暂不支持")
            }
            // 抑制 NativeApi 提示
            it.compilerOptions.options.optIn.addAll(
                "kotlinx.cinterop.ExperimentalForeignApi",
                "kotlin.experimental.ExperimentalNativeApi",
            )
        }
    }
```
3. 实现ohosArm64Main
参考mmkvKotlin ohosArm64Main实现

### 版本发布
本组件已发布到各平台标准仓库。如果开发者需要发布到自己私仓，可参考本节指引发布。 
#### mmkvKotlin 模块
##### Android/iOS/Ohos跨端产物发布
1. 配置发布仓库：

   修改build.gradle.kts发布仓库配置
```script
publishing {
...
            mavenLocal()
            // 发布到build/repo目录
//            maven {
//                url = uri(layout.buildDirectory.dir("repo"))
//            }
        }
...
}
```
2. 配置 maven 发布所需参数环境变量

   参考KotlinbuildVar.kt
```kotlin
fun MavenPublication.signPublicationIfKeyPresent(project: Project) {
   val keyId = getSensitiveProperty(project, "signing_keyId")
   val secretKey = getSensitiveProperty(project, "signing_secretKey")
   val password = getSensitiveProperty(project, "signing_password")
   println("signPublicationIfKeyPresent keyId:  $keyId , password: $password , secreckey: $secretKey")
   if (!secretKey.isNullOrBlank()) {
      project.extensions.configure<SigningExtension>("signing") {
         useInMemoryPgpKeys(keyId, secretKey, password)
         sign(this@signPublicationIfKeyPresent)
      }
   }
}
```
3. 配置发布版本：

   MavenConfig.VERSION
4. 运行mmkvKotlin模块的 publish任务

:::tip 注意

如果业务mmkvKotlin模块采用鸿蒙独立Kotlin编译工具链方式，则需要在build.gradle.kts中移除ohosArm64 target配置，只对Android/iOS目标进行发布。
Ohos目标发布则需要参考下述方式单独发布。

:::

##### Ohos跨端产物发布（Ohos独立Kotlin编译工具链方式）
如果业务mmkvKotlin模块采用独立Kotlin编译工具链方案，则要单独对鸿蒙产物进行发布。
1. 配置发布仓库

   修改build.ohos.gradle.kts发布仓库配置
2. 配置 maven 发布所需参数环境变量(同上一步)
3. 配置发布版本：MavenConfig.VERSION_OHOS

   注意，Ohos跨端产物必须独立版本号，如本工程是在版本号后增加-ohos标识。如果不独立版本号，在运行上一步安卓/iOS跨端产物发布后，再运行Ohos发布脚本，则分两次发布会引起maven元数据混乱。
4. 运行发布脚本

   ./gradlew -c settings.ohos.gradle.kts :mmkvKotlin:publish
