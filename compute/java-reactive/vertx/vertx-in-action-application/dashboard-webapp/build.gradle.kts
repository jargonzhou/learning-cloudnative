dependencies {
    implementation(project(":commons"))

    implementation("io.vertx:vertx-rx-java3")
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-kafka-client") {
        exclude("org.slf4j")
        exclude("log4j")
    }
}
