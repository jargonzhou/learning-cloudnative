plugins {
    id("java")
}

group = "com.spike.eventstreams"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // This dependency is used by the application.
    implementation(libs.guava)

    // https://mvnrepository.com/artifact/org.apache.avro/avro
    implementation("org.apache.avro:avro:1.11.3")
    // https://mvnrepository.com/artifact/org.apache.avro/avro-tools
    testImplementation("org.apache.avro:avro-tools:1.11.3") // WARN: huge jar
    {
        exclude("org.slf4j", "slf4j-log4j12")
    }
}

tasks.test {
    useJUnitPlatform()
}

// TODO: add Gradle tasks for compile Avro specifications
//tasks.add(Task)