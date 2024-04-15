plugins {
    id("java")
}

group = "com.spike.eventstreams"
version = "1.0.0"

repositories {
    mavenCentral()
}

// samza-hello-samza\gradle.properties
val SAMZA_VERSION = "1.8.0"
//val KAFKA_VERSION = "0.11.0.2"
val HADOOP_VERSION = "2.7.1"

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // This dependency is used by the application.
    implementation(libs.guava)

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:1.7.36")
    runtimeOnly("ch.qos.logback:logback-core:1.5.4")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.4")

    // A Java Internet Relay Chat library
    // https://mvnrepository.com/artifact/org.schwering/irclib
    testImplementation("org.schwering:irclib:1.10")

    implementation("org.apache.samza:samza-api:$SAMZA_VERSION")
//    implementation("org.apache.samza:samza-aws_2.12:$SAMZA_VERSION")
//    implementation("org.apache.samza:samza-azure_2.12:$SAMZA_VERSION")
    implementation("org.apache.samza:samza-kafka_2.12:$SAMZA_VERSION")
    implementation("org.apache.samza:samza-kv_2.12:$SAMZA_VERSION")
    implementation("org.apache.samza:samza-kv-couchbase_2.12:$SAMZA_VERSION")
    implementation("org.apache.samza:samza-kv-rocksdb_2.12:$SAMZA_VERSION")
    runtimeOnly("org.apache.samza:samza-core_2.12:$SAMZA_VERSION")
    runtimeOnly("org.apache.samza:samza-log4j2_2.12:$SAMZA_VERSION")
//    runtimeOnly("org.apache.samza:samza-shell:$SAMZA_VERSION")
    runtimeOnly("org.apache.samza:samza-yarn_2.12:$SAMZA_VERSION")
//    implementation("org.apache.kafka:kafka_2.12:$KAFKA_VERSION")
//    runtimeOnly("org.apache.hadoop:hadoop-hdfs:$HADOOP_VERSION")
    testImplementation("org.apache.samza:samza-test_2.12:$SAMZA_VERSION")

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
}