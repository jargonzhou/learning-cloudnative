import com.google.protobuf.gradle.*

plugins {
    application
    // https://github.com/google/protobuf-gradle-plugin/tree/master/examples/exampleKotlinDslProject
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)

    runtimeOnly("io.grpc:grpc-netty-shaded:1.65.0")
    implementation("io.grpc:grpc-protobuf:1.65.0")
    implementation("io.grpc:grpc-stub:1.65.0")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53") // necessary for Java 9+

    testImplementation("io.grpc:grpc-testing:1.65.0")
    testImplementation("io.grpc:grpc-inprocess:1.65.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.4.0")

    // JsonFormat
    implementation("com.google.protobuf:protobuf-java-util:3.25.1")

}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("com.spike.grpc.App")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
//    useJUnitPlatform()
    useJUnit()
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.4"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.65.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc") {}
            }
        }
    }
}