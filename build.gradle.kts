buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPaths.gradle)
        classpath(ClassPaths.kotlin)
        classpath(ClassPaths.googleService)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}