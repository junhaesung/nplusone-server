import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":nplusone-domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.withType<Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = true
    archiveName = "nplusone-api.jar"
}