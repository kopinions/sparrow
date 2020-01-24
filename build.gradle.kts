plugins {
    `java-library`
    java
    idea
}

val junit_version: String  = "5.6.0"

allprojects {
    apply {
        plugin("java")
        plugin("idea")
        plugin("java-library")
    }
    repositories {
        jcenter()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:$junit_version")
    }

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
