
allprojects {
    task("printProjectName") {
        doLast() {
            println(project.name)
        }
    }
}