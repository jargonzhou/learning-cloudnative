import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    `java-library`
}

val vertxVersion = "4.5.4"
val junitJupiterVersion = "5.9.1"

group = "com.spike.vertx"
version = "1.0.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}


subprojects {
    apply(plugin = "java")
    apply(plugin = "application")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
        // core: https://vertx.io/docs/vertx-core/java/
        // implementation("io.vertx:vertx-core")

        // config: https://vertx.io/docs/vertx-config/java/
        implementation("io.vertx:vertx-config")
        implementation("io.vertx:vertx-config-yaml")

        // rx-java3: https://vertx.io/docs/vertx-rx/java3/
        // implementation("io.vertx:vertx-rx-java3")

        // logging: https://vertx.io/docs/vertx-core/java/#_logging
        implementation("ch.qos.logback:logback-classic:1.5.3")

        // web: https://vertx.io/docs/vertx-web/java/
        // implementation("io.vertx:vertx-web")
        // web client: https://vertx.io/docs/vertx-web-client/java/
        // implementation("io.vertx:vertx-web-client")
        // JWT: https://vertx.io/docs/vertx-auth-jwt/java/
        // implementation("io.vertx:vertx-auth-jwt")

        // mail client: https://vertx.io/docs/vertx-mail-client/java/
        // implementation("io.vertx:vertx-mail-client")

        // postgresql: https://vertx.io/docs/vertx-pg-client/java/
        // implementation("io.vertx:vertx-pg-client")

        // AMQP: https://vertx.io/docs/vertx-amqp-client/java/
        // implementation("io.vertx:vertx-amqp-client")

        // kafka: https://vertx.io/docs/vertx-kafka-client/java/
        // implementation("io.vertx:vertx-kafka-client") {
        //  exclude("org.slf4j")
        //  exclude("log4j")
        // }

        // mongodb: https://vertx.io/docs/vertx-mongo-client/java/
        // implementation("io.vertx:vertx-mongo-client")
        // mongodb auth provider: https://vertx.io/docs/vertx-auth-mongo/java/
        // implementation("io.vertx:vertx-auth-mongo")

        // utility
        implementation("com.google.guava:guava:33.1.0-jre")

        // Vert.x JUnit 5 integration: https://vertx.io/docs/vertx-junit5/java/
        testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
        // testImplementation("io.vertx:vertx-junit5")
        // testImplementation("io.vertx:vertx-junit5-rx-java3")
        // AssertJ - fluent assertions java library: https://assertj.github.io/doc/#assertj-core
        testImplementation("org.assertj:assertj-core:3.25.3")
        // REST-assured: https://github.com/rest-assured/rest-assured
        testImplementation("io.rest-assured:rest-assured:5.4.0")
        // Testcontainers for Java: https://java.testcontainers.org/
        // testImplementation("org.testcontainers:junit-jupiter:1.19.7")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events = setOf(PASSED, SKIPPED, FAILED)
        }
    }
}
