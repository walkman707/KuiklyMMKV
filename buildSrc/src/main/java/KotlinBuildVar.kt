import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension
import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Version {

    private const val KUIKLY_VERSION = "2.4.0"
    private const val KOTLIN_VERSION = "2.0.21"
    private const val KOTLIN_OHOS_VERSION = "2.0.21-ohos"

    /**
     * 获取 Kuikly 版本号，版本号规则：${shortVersion}-${kotlinVersion}
     * 适用于 core、core-ksp、core-annotation、core-render-android
     */
    fun getKuiklyVersion(): String {
        return "$KUIKLY_VERSION-$KOTLIN_VERSION"
    }

    /**
     * 获取 Kuikly Ohos版本号
     */
    fun getKuiklyOhosVersion(): String {
        return "$KUIKLY_VERSION-$KOTLIN_OHOS_VERSION"
    }
}

object BuildPlugin {
    val kuikly by lazy {
        "com.tencent.kuikly-open:core-gradle-plugin:${Version.getKuiklyVersion()}"
    }
}

object MavenConfig {
    const val GROUP = "io.github.walkman707"
    const val VERSION = "1.0.1"
    const val VERSION_OHOS = "$VERSION-ohos"
    const val REPO_URL = "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2"
    const val SNAPSHOT_REPO_URL = "https://central.sonatype.com/repository/maven-snapshots/"
    private const val KEY_USER_NAME = "username"
    private const val KEY_USER_PASSWORD = "password"

    fun getUsername(project: Project) : String {
        var username = ""
        if (project.hasProperty(KEY_USER_NAME)) {
            return project.property(KEY_USER_NAME) as String
        }
        if (username.isEmpty()) {
            val propertiesFile = File(project.rootDir, "local.properties")
            if (!propertiesFile.exists()) {
                return ""
            }
            val prop = Properties().apply {
                load(FileInputStream(propertiesFile))
            }
            username = prop.getProperty(KEY_USER_NAME) ?: ""
        }
        println("username is $username")
        return username
    }

    fun getPassword(project: Project) : String {
        var password = ""
        if (project.hasProperty(KEY_USER_PASSWORD)) {
            return project.property(KEY_USER_PASSWORD) as String
        }
        if (password.isEmpty()) {
            val propertiesFile = File(project.rootDir, "local.properties")
            if (!propertiesFile.exists()) {
                return ""
            }
            val prop = Properties().apply {
                load(FileInputStream(propertiesFile))
            }
            password = prop.getProperty(KEY_USER_PASSWORD) ?: ""
        }
        return password
    }

    fun getRepoUrl(version: String) : String {
        if (version.endsWith("-SNAPSHOT")) {
            return SNAPSHOT_REPO_URL
        }
        return REPO_URL
    }
}

fun MavenPom.configureMavenCentralMetadata() {
    name.set("KotlinMMKV")
    description.set("`KotlinMMKV` is a mmkv component that support Android/iOS/Ohos")
    url.set("https://github.com/walkman707/x9nfof3keg")

    licenses {
        license {
            name.set("Apache-2.0 license")
            url.set("https://github.com/walkman707/x9nfof3keg/blob/main/LICENSE")
        }
    }

    developers {
        developer {
            id.set("walkman707")
            name.set("walkman707")
        }
    }
    scm {
        url.set("https://github.com/walkman707/x9nfof3keg")
    }
}

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

fun getSensitiveProperty(project: Project, name: String): String? {
    return project.findProperty(name) as? String ?: System.getenv(name)
}