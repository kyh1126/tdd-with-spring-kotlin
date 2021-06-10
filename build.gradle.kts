import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.10"
    kotlin("kapt") version "1.5.10"
    kotlin("plugin.allopen") version "1.5.10"
    kotlin("plugin.spring") version "1.5.10"
    kotlin("plugin.jpa") version "1.5.10"
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

group = "me.jenny"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

extra["testcontainersVersion"] = "1.15.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.querydsl:querydsl-jpa:4.4.0")

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("it.ozimov:embedded-redis:0.7.3") {  // 0.7.2 때 없던 의존성이 추가되어 에러가 발생 (LoggerFactory is not a Logback LoggerContext but Logback is on the classpath)
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

//    implementation("io.springfox:springfox-swagger2:2.9.2")         // 이만 2점대 스웨거를 벗어나보려고 한다.
//    implementation("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("io.springfox:springfox-boot-starter:3.0.0")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    kapt("com.querydsl:querydsl-apt:4.4.0:jpa")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.mockito:mockito-inline:3.6.28")
    // @Testcontainers 관련 설정
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
