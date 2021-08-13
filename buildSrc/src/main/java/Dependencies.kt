import org.gradle.api.JavaVersion

object Config {
    const val application_id = "com.katyrin.dictionaryapp"
    const val compile_sdk = 30
    const val build_tools = "30.0.3"
    const val min_sdk = 21
    const val target_sdk = 30
    val java_version = JavaVersion.VERSION_11
}

object Releases {
    const val version_code = 1
    const val version_name = "1.0"
}

object Modules {
    const val app = ":app"
    const val core = ":core"
    const val model = ":model"
    const val repository = ":repository"
    const val utils = ":utils"
    const val historyScreen = ":historyScreen"
}

object Versions {

    // Design
    const val appcompat = "1.3.1"
    const val material = "1.4.0"
    const val constraintLayout = "2.1.0"
    const val swipeRefreshLayout = "1.1.0"

    // Kotlin
    const val core = "1.6.0"
    const val stdlib = "1.5.21"
    const val coroutinesCore = "1.5.1"
    const val coroutinesAndroid = "1.5.1"

    // Retrofit
    const val retrofit = "2.9.0"
    const val converterGson = "2.9.0"
    const val interceptor = "4.9.1"

    // Koin
    const val koinAndroid = "2.2.1"
    const val koinViewModel = "2.2.1"
    const val koinScope = "2.2.1"
    const val koinFragment = "2.2.1"

    // Glide
    const val glide = "4.12.0"
    const val glideCompiler = "4.12.0"

    // Room
    const val roomKtx = "2.3.0"
    const val runtime = "2.3.0"
    const val roomCompiler = "2.3.0"

    // Test
    const val jUnit = "4.13.2"
    const val testJUnit = "1.1.3"
    const val espressoCore = "3.4.0"

    //Google Play
    const val googlePlayCore = "1.8.1"
}


object Design {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val swipe_refresh_layout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
}

object Kotlin {
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.stdlib}"
    const val coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    const val coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.converterGson}"
    const val logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
}

object Koin {
    const val koin_android = "org.koin:koin-android:${Versions.koinAndroid}"
    const val koin_view_model = "org.koin:koin-androidx-viewmodel:${Versions.koinViewModel}"
    const val koin_scope = "org.koin:koin-androidx-scope:${Versions.koinScope}"
    const val koin_fragment = "org.koin:koin-androidx-fragment:${Versions.koinFragment}"
}

object Glide {
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glideCompiler}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.runtime}"
    const val compiler = "androidx.room:room-compiler:${Versions.roomCompiler}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.roomKtx}"
}

object TestImpl {
    const val junit = "junit:junit:${Versions.jUnit}"
    const val test_junit = "androidx.test.ext:junit:${Versions.testJUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
}

object GooglePlay {
    const val googlePlayCore = "com.google.android.play:core-ktx:${Versions.googlePlayCore}"
}