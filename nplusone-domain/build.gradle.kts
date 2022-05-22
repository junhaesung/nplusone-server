import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("mysql:mysql-connector-java")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")
    testImplementation("com.h2database:h2")
}

tasks.withType<Jar> {
    enabled = true
}

tasks.withType<BootJar> {
    enabled = false
}