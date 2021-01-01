import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
}

group = "com.example.springboot"
version = "0.1.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework:spring-websocket")
    implementation("org.springframework:spring-messaging")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.slack.api:slack-api-model:1.4.1")
    implementation("com.slack.api:slack-api-client:1.4.1")

    // if you use spring-boot-starter-web, you don't have to include tyrus-standalone-client,
    // because tomcat-embed-websocket has the implementation of javax.websocket.
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:1.17")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
