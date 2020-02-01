val jackson_version: String  = "2.10.2"
val jackson_joda_version: String  = "2.10.2"

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:$jackson_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:$jackson_joda_version")
}

