import org.springframework.boot.gradle.tasks.run.BootRun

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // config client
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    // retry for config client
    implementation("org.springframework.retry:spring-retry")

    // JDBC
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    // PostgreSQL
    runtimeOnly("org.postgresql:postgresql")

    // OAuth2 resource server
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // for WebTestClient
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")

    // testcontainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:rabbitmq")
}

tasks.named<BootRun>("bootRun") {
    // DEV_ENV: profile testdata
    systemProperty("spring.profiles.active", "testdata")
//    systemProperty("spring.profiles.active", "prod")
}


