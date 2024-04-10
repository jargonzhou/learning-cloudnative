// https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins.html#build-tool-plugins.gradle
plugins {
    `java-library`
    id("org.springframework.boot") version "3.2.4" // apply false
    id("io.spring.dependency-management") version "1.1.4"
    id("org.hibernate.orm") version "6.4.4.Final"
//    id("org.graalvm.buildtools.native") version "0.9.28"
}

extra["springCloudVersion"] = "2023.0.1"

group = "com.spike.spring"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.hibernate.orm")
//    apply(plugin = "org.graalvm.buildtools.native")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-actuator") // actuator

//        implementation("org.springframework.boot:spring-boot-starter-amqp")
//        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
//        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//        implementation("org.springframework.boot:spring-boot-starter-data-r2dbc") // reactive
//        implementation("org.springframework.boot:spring-boot-starter-data-redis")
//        implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive") // reactive, redis
//        implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server") // OAuth2 authorization server
//        implementation("org.springframework.boot:spring-boot-starter-oauth2-client") // OAuth2 client
//        implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server") // OAuth2 resource server
//        implementation("org.springframework.boot:spring-boot-starter-security") // security
//        implementation("org.springframework.boot:spring-boot-starter-validation") // validation
//        implementation("org.springframework.boot:spring-boot-starter-web")
//        implementation("org.springframework.boot:spring-boot-starter-webflux") // reactive
//        implementation("io.micrometer:micrometer-tracing-bridge-brave")
//        implementation("org.flywaydb:flyway-core")
//        implementation("org.springframework.amqp:spring-rabbit-stream")
//        implementation("org.springframework.cloud:spring-cloud-bus")
//        implementation("org.springframework.cloud:spring-cloud-config-server")
//        implementation("org.springframework.cloud:spring-cloud-function-web")
//        implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j") // circuit breaker
//        implementation("org.springframework.cloud:spring-cloud-starter-config")
//        implementation("org.springframework.cloud:spring-cloud-starter-gateway") // reactive, gateway
//        implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc")
//        implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
//        implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
//        implementation("org.springframework.cloud:spring-cloud-stream")
//        implementation("org.springframework.cloud:spring-cloud-stream-binder-rabbit")
//        implementation("org.springframework.session:spring-session-data-redis") // session

        compileOnly("org.projectlombok:lombok")
        developmentOnly("org.springframework.boot:spring-boot-devtools") // devtools

//        runtimeOnly("com.h2database:h2")
//        runtimeOnly("io.micrometer:micrometer-registry-prometheus")
//        runtimeOnly("io.r2dbc:r2dbc-h2")
//        runtimeOnly("org.postgresql:postgresql")
//        runtimeOnly("org.postgresql:r2dbc-postgresql")

        // the annotation processor: META-INF/spring-configuration-metadata.json
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        annotationProcessor("org.projectlombok:lombok")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
//        testImplementation("org.springframework.boot:spring-boot-testcontainers")
//        testImplementation("io.projectreactor:reactor-test") // reactive
//        testImplementation("org.springframework.amqp:spring-rabbit-test")
//        testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
//        testImplementation("org.springframework.security:spring-security-test")
//        testImplementation("org.testcontainers:junit-jupiter")
//        testImplementation("org.testcontainers:postgresql")
//        testImplementation("org.testcontainers:r2dbc")
//        testImplementation("org.testcontainers:rabbitmq")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    hibernate {
        enhancement {
            enableAssociationManagement.set(true)
        }
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }
}








