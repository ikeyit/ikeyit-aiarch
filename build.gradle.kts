tasks.register("build") {
    gradle.includedBuilds.map {
        dependsOn(it.task(":build"))
    }
}

tasks.register("clean") {
    gradle.includedBuilds.map {
        dependsOn(it.task(":clean"))
    }
}