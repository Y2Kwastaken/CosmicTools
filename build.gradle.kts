plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "sh.miles.cosmictools"
version = "2.0.1-SNAPSHOT"

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
    dependsOn(tasks.shadowJar)
}
