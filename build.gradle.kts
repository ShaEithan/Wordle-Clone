plugins {
  java
  jacoco
  pmd
}       

repositories {
	mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
	testImplementation("org.junit.platform:junit-platform-console:1.7.1")
    testImplementation("junit:junit:4.13.1")
  testImplementation("junit:junit:4.13.1")
}

tasks {
  val flags = listOf("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

  getByName<JavaCompile>("compileJava") {
    options.compilerArgs = flags
  }

  getByName<JavaCompile>("compileTestJava") {
    options.compilerArgs = flags
  }
}
 
sourceSets {
  main {
    java.srcDirs("src")
  }
  test {
    java.srcDirs("test")
  }
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform {}
//	jvmArgs("--enable-preview")
}

pmd {
    ruleSets = listOf()
    ruleSetFiles = files("../conf/pmd/ruleset.xml")
    toolVersion = "6.54.0"    
}                                                

tasks.withType<JacocoReport> {
  afterEvaluate {
    classDirectories.setFrom(files(classDirectories.files.map {
      fileTree(it).apply {
        exclude("game/ui")
      }
    }))
  }
}

tasks.register("ui", JavaExec::class) {
  classpath = sourceSets.getByName("main").runtimeClasspath
  mainClass.set("game.ui.WordleGrid")
}

defaultTasks("clean", "test", "jacocoTestReport", "pmdMain")
