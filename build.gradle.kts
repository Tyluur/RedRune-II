plugins {
    application
    kotlin("jvm") version "1.3.72"
}

val koinVersion = "2.1.5"
val junitVersion = "5.6.2"
val jacksonVersion = "2.11.0"

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    version = "0.0.1"

    java.sourceCompatibility = JavaVersion.toVersion('8')
    java.targetCompatibility = JavaVersion.toVersion('8')

    repositories {
        mavenCentral()
        mavenLocal()
        jcenter()
        maven(url = "https://repo.maven.apache.org/maven2")
        maven(url = "https://jitpack.io")
        maven(url = "https://dl.bintray.com/michaelbull/maven")
    }
}

application {
    mainClassName = "org.redrune.Bootstrap"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("io.netty:netty-all:4.1.44.Final")
    implementation(group = "com.displee", name = "rs-cache-library", version = "6.7")
    implementation(group = "org.yaml", name = "snakeyaml", version = "1.26")
    implementation(
        group = "com.michael-bull.kotlin-inline-logger",
        name = "kotlin-inline-logger-jvm",
        version = "1.0.2"
    )
    implementation(group = "org.koin", name = "koin-core", version = koinVersion)
    implementation(group = "org.koin", name = "koin-logger-slf4j", version = koinVersion)
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.4.2")

    //Logging
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    //Utilities
    implementation("com.google.guava:guava:29.0-jre")
    implementation("org.apache.commons:commons-lang3:3.10")
    implementation("commons-cli", "commons-cli", "1.4")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("org.postgresql:postgresql:42.2.12")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("it.unimi.dsi:fastutil:8.3.1")
    implementation("com.github.michaelbull", "rs-api", "1.1.1")
    implementation("com.zaxxer", "HikariCP", "2.3.2")

    //Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation(group = "org.koin", name = "koin-test", version = koinVersion)
    testImplementation(group = "io.mockk", name = "mockk", version = "1.10.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
/*plugins {
    id "application"
}
apply plugin : "java"
ext {
    javaMainClass = "org.redrune.Bootstrap"
}

application {
    mainClassName = javaMainClass
}

group 'dusk.rs'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {

    compile 'com.github.michaelbull:rs-api:1.1.1'

    // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
    compile group: 'com.zaxxer', name: 'HikariCP', version: '2.3.2'

    // https://mvnrepository.com/artifact/io.netty/netty-all
    compile group: 'io.netty', name: 'netty-all', version: '4.1.33.Final'

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.25'

    // https://mvnrepository.com/artifact/com.google.guava/guava
    compile group: 'com.google.guava', name: 'guava', version: '19.0'

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    compile group: 'com.google.code.gson', name: 'gson', version: '2.7'

    // https://mvnrepository.com/artifact/mysql/mysql-connector-java
    compile group: 'mysql', name: 'mysql-connector-java', version: '8.0.15'

    // https://mvnrepository.com/artifact/commons-cli/commons-cli
    compile group: 'commons-cli', name: 'commons-cli', version: '1.4'

    testCompile group: 'junit', name: 'junit', version: '4.12'
}
 */