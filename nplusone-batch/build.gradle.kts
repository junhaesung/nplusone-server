import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":nplusone-domain"))
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.seleniumhq.selenium:selenium-java:4.1.3")
    implementation("io.github.bonigarcia:webdrivermanager:5.1.0")
    implementation("org.jsoup:jsoup:1.16.1")
}

tasks.withType<Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = true
    archiveName = "nplusone-batch.jar"
}
