dependencies {
    implementation(project(":commons"))

    implementation("io.vertx:vertx-rx-java3")
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-web-client")
    implementation("io.vertx:vertx-auth-jwt")

    testImplementation("io.vertx:vertx-junit5-rx-java3")
    testImplementation("io.vertx:vertx-pg-client")
    testImplementation("io.vertx:vertx-mongo-client")
    testImplementation(project(":user-profile-service"))
    testImplementation(project(":activity-service"))

}