buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.4.2")
    }
}

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "sh.miles.cosmictools"
version = "2.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations-java5:24.0.1")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
    implementation("org.seleniumhq.selenium:selenium-java:4.18.1")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    enabled = false
    manifest {
        attributes(
            mapOf("Main-Class" to "sh.miles.cosmictools.CosmicTools")
        )
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveVersion.set("")

    minimize {
        this.exclude {
            it.name.contains("org.seleniumhq.selenium")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.getByPath("proguard"))
}

tasks.register<proguard.gradle.ProGuardTask>("proguard") {
    verbose()

    injars(tasks.shadowJar)

    outjars("build/proguard/CosmicTools.jar")

    val javaHome = System.getProperty("java.home")
    // Automatically handle the Java version of this build.
    libraryjars(
        // filters must be specified first, as a map
        mapOf("jarfilter" to "!**.jar",
            "filter"    to "!module-info.class"),
        "$javaHome/jmods/java.base.jmod"
    )

    keepclassmembers("enum * { *; }")
    // keep(" class org.openqa.selenium.** { *; }")
    keep("class org.openqa.selenium.manager.** { *; }")
    keep("class org.openqa.selenium.remote.http.** { *; }")
    keep("class org.openqa.selenium.devtools.v120.v120CdpInfo { *; }")
    allowaccessmodification()

    dontwarn()
    dontobfuscate()

    keep("""
        class sh.miles.cosmictools.CosmicTools {
            public static void main(java.lang.String[]);
        }
    """)
}
