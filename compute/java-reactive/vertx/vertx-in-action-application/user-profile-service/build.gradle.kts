dependencies {
    implementation(project(":commons"))

    implementation("io.vertx:vertx-rx-java3")
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-mongo-client")
    implementation("io.vertx:vertx-auth-mongo")

    testImplementation("io.vertx:vertx-junit5-rx-java3")
}