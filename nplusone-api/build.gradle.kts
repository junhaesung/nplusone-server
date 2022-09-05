import org.springframework.boot.gradle.tasks.bundling.BootJar

val javaJwtVersion: String by project

dependencies {
    implementation(project(":nplusone-domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("com.auth0:java-jwt:$javaJwtVersion")
    testImplementation("com.google.firebase:firebase-admin:9.0.0")
}

tasks.withType<Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = true
    archiveName = "nplusone-api.jar"
}