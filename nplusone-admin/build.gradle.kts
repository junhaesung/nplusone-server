import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":nplusone-domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.withType<Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = true
    archiveName = "nplusone-admin.jar"
}
