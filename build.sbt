name := "dice"

organization := "games.datastrophic"

scalaVersion := "2.13.2"

crossScalaVersions := List("2.12.11", "2.13.2")

scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation", "-feature", "-Xmacro-settings:materialize-derivations",
  "-unchecked", "-language:implicitConversions", "-language:postfixOps", "-language:higherKinds")

scalacOptions ++= (if (priorTo2_13(scalaVersion.value)) Seq("-Ypartial-unification") else Nil)

javaOptions +="-Duser.timezone=GMT"

libraryDependencies ++= Seq(

)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0"  cross CrossVersion.full)

enablePlugins(GitVersioning)

useJGit
git.useGitDescribe := true
git.gitTagToVersionNumber := { tag: String => Some(tag)}

updateOptions := updateOptions.value.withGigahorse(false)


def priorTo2_13(scalaVersion: String): Boolean =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, minor)) if minor < 13 => true
    case _                              => false
  }
