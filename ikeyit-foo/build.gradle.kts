// AI-NOTE: Add the following code to execute the task for all subproject for convenience
tasks.register("clean") {
    dependsOn(subprojects.map { it.tasks.named("clean") })
}
tasks.register("build") {
    dependsOn(subprojects.map { it.tasks.named("build") })
}
tasks.register("publish") {
    subprojects {
        tasks.findByName("publish")?.also{
            dependsOn(it)
        }
    }
}