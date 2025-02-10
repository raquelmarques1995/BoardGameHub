plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.finalprojectandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.finalprojectandroid"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    viewBinding{
        enable = true
    }

    dataBinding{
        enable = true
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Dependências principais
    implementation("androidx.appcompat:appcompat:1.6.1") // Suporte para compatibilidade com versões anteriores do Android
    implementation("com.google.android.material:material:1.9.0") // Material Design Components

    // Dependências para Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Biblioteca principal Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Conversor Gson para manipulação de JSON
    implementation("com.squareup.retrofit2:converter-simplexml:2.9.0")

    //Dependências para Glide para carregar imagens
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")


    // Dependências adicionais
    implementation("androidx.recyclerview:recyclerview:1.3.0") // RecyclerView para listas
    implementation("com.google.code.gson:gson:2.9.0") // Biblioteca Gson para JSON

    // Dependências para testes
    testImplementation("junit:junit:4.13.2") // Testes unitários com JUnit
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Extensões de teste para JUnit no Android
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Testes de UI com Espresso
}