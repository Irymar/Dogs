import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
}

group = "academy.softserve"
version = "0.0.1-SNAPSHOT"
java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

// Завдання для встановлення npm залежностей
tasks.register<Exec>("npmInstall") {
    workingDir = file("ui")
    commandLine("C:\\Program Files\\nodejs\\npm.cmd", "i") // Використовуємо npm з вказаним шляхом
}

// Завдання для компіляції UI
tasks.register<Exec>("compileUi") {
    workingDir = file("ui")
    commandLine("C:\\Program Files\\nodejs\\npm.cmd", "run", "build") // Використовуємо npm з вказаним шляхом
    dependsOn("npmInstall") // Спочатку виконується завдання для встановлення залежностей
}

// Завдання для копіювання збірки UI в resources
tasks.register<Copy>("copyUi") {
    from("ui/dist") {
        include("index.html")
        include("app.js")
    }
    from("ui/dist") {
        include("css/**")
    }
    into("src/main/resources/static")
    dependsOn("compileUi") // Завдання для копіювання залежить від compileUi
}

// Налаштування залежності для bootRun
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    dependsOn("copyUi") // Завдання для запуску також залежить від copyUi
}
