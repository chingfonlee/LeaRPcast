pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LeaRPcast"

include(":app")
include(":core:common")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:media")
include(":core:model")
include(":core:testing")
include(":domain")
include(":feature:radio")
include(":feature:podcast")
include(":feature:player")
include(":feature:downloads")
include(":feature:settings")
