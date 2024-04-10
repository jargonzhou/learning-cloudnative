dependencies {
    // gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    // resilience4j
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    // session
    implementation("org.springframework.session:spring-session-data-redis")
    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")
    // oauth client
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    testImplementation("io.projectreactor:reactor-test")

    // testcontainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
}