import jetbrains.buildServer.configs.kotlin.*

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.2"

fun ProjectFeatures.addGraphs(buildTypeList : List<BuildType>) {

    buildTypeList.forEach {

        feature {

            type = "project-graphs"

            param(

                "series", """
                    [
                      {
                        "type": "valueType",
                        "title": "Time spent in queue",
                        "sourceBuildTypeId": ${it.id},
                        "key": "TimeSpentInQueue"
                      },
                      {
                        "type": "valueType",
                        "title": "Build duration",
                        "sourceBuildTypeId": ${it.id},
                        "key": "BuildDuration"
                      }
                    ]
                """.trimIndent()

            )

            param("format", "duration")

            param("hideFilters", "")

            param("title", "Time spent overall ${it.name}")

            param("defaultFilters", "showFailed")

            param("seriesTitle", "Serie")

        }

        feature {

            type = "project-graphs"

            param(

                "series", """
                    [
                      {
                        "type": "valueType",
                        "title": "Success Rate for ${it.name}",
                        "sourceBuildTypeId": ${it.id},
                        "key": "SuccessRate"
                      }
                    ]
                """.trimIndent()

            )

            param("format", "percentBy1")

            param("hideFilters", "")

            param("title", "Success Rate for ${it.name}")

            param("defaultFilters", "showFailed, averaged")

            param("seriesTitle", "Serie")

        }

    }

    feature {

        type = "buildtype-graphs"

        param(

            "series", """
                    [
                      {
                        "type": "valueTypes",
                        "pattern": "buildStageDuration:*"
                         }
                    ]
            """.trimIndent()

        )

        param("format", "duration")

        param("hideFilters", "")

        param("title", "Time per step")

        param("defaultFilters", "showFailed")

        param("seriesTitle", "Serie")

    }

}



version = "2021.1"



project {



    buildType(Buildconfig1)

    buildType(Secondaconfig1)

    val buildChain = sequential  {

      buildType(Buildconfig1)

      buildType(Secondaconfig1)

    }

    features {

    addGraphs(buildChain.buildTypes())

    }

}



object Buildconfig1 : BuildType({

    name = "buildconfig"



    steps {

        script {

            scriptContent = "echo a"

        }

    }

})



object Secondaconfig1 : BuildType({

    name = "Secondaconfig"



 steps {

        script {

            name = "step1"

            scriptContent = "echo a"

        }

        script {

            name = "step2"

            scriptContent = "echo a"

        }

    }

})


