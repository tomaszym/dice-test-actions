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

organization := "games.datastrophic"
homepage := Some(url("https://github.com/datastrophic-games/dice"))
scmInfo := Some(ScmInfo(url("https://github.com/datastrophic-games/dice"), "git@github.com:datastrophic-games/dice.git"))
developers := List(Developer("tomaszym", "tomaszym", "tomaszym@pm.me", url("https://github.com/tomaszym")))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true
description := "Immutable, PCG-based random numbers generator"

credentials += {
  if (sys.env.isDefinedAt("CI_SERVER")) {
    Credentials(
      realm = "Sonatype Nexus Repository Manager",
      host = "oss.sonatype.org",
      userName = sys.env.apply("SONATYPE_USER"),
      passwd = sys.env.apply("SONATYPE_PASSWORD")
    )
  } else {
    Credentials(Path.userHome / ".sbt" / ".sonatype")
  }
}

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}


def priorTo2_13(scalaVersion: String): Boolean =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, minor)) if minor < 13 => true
    case _                              => false
  }
