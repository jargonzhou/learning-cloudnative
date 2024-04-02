plugins {
    `java-library`
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.spike.security.spring"
version = "0.0.1-SNAPSHOT"

extra["springCloudVersion"] = "2023.0.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//        implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
//	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
//	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//        implementation("org.springframework.boot:spring-boot-starter-security")
//	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
//        implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.springframework.boot:spring-boot-starter-webflux")
//	implementation("org.flywaydb:flyway-core")
//	implementation("org.flywaydb:flyway-mysql")
//	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
//	implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc")
//	implementation("org.springframework.session:spring-session-core")
//	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        compileOnly("org.projectlombok:lombok")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
//	runtimeOnly("com.mysql:mysql-connector-j")
//	runtimeOnly("io.asyncer:r2dbc-mysql")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        annotationProcessor("org.projectlombok:lombok")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("io.projectreactor:reactor-test")
        testImplementation("org.springframework.security:spring-security-test")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}


