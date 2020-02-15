plugins {
    application
}

dependencies {
    runtimeOnly(project(":modules:os"))
}

application {
    mainClassName = "com.kopinions.JarBIOS"
}
