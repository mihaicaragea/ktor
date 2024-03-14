import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
  id("com.github.johnrengelman.shadow") version "8.1.1"
  kotlin("jvm") version "1.9.22"
  id("io.ktor.plugin") version "2.3.8"
}

group = "com.example"
version = "0.0.1"

var mainClassName = "com.learning.ApplicationKt"

application {
  mainClass.set("mainClassName")

  val isDevelopment: Boolean = project.ext.has("development")
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

val shadowJar: ShadowJar by tasks
shadowJar.apply {
  manifest.attributes.apply {
    put("Main-Class", mainClassName)
  }
  archiveFileName.set("app.jar")
}


repositories {
  mavenCentral()
  maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

dependencies {
  implementation("io.ktor:ktor-server-locations:$ktor_version")
  implementation("io.ktor:ktor-server-core-jvm")
  implementation("io.ktor:ktor-server-html-builder-jvm")
  implementation("org.jetbrains.kotlinx:kotlinx-html:0.10.1")
  implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.129-kotlin-1.4.20")
  implementation("io.ktor:ktor-server-call-logging-jvm")
  implementation("io.ktor:ktor-server-host-common-jvm")
  implementation("io.ktor:ktor-server-status-pages-jvm")
  implementation("io.ktor:ktor-server-partial-content-jvm")
  implementation("io.ktor:ktor-server-auth-jvm")
  implementation("io.ktor:ktor-server-content-negotiation-jvm")
  implementation("io.ktor:ktor-serialization-gson-jvm")
  implementation("io.ktor:ktor-serialization-jackson-jvm")
  implementation("io.ktor:ktor-server-netty-jvm")
  implementation("ch.qos.logback:logback-classic:$logback_version")
  implementation("org.mongodb:mongodb-driver-sync:4.11.1")
  implementation("io.ktor:ktor-server-sessions:$ktor_version")
  testImplementation("io.ktor:ktor-server-tests-jvm")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
