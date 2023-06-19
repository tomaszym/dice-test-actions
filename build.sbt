name := "dice"

organization := "games.datastrophic"

scalaVersion := "3.0.0"

crossScalaVersions := List("2.13.11", "3.3.0")

scalacOptions := Seq(
  "-encoding", "UTF-8", "-deprecation", "-feature",
  "-unchecked", "-language:implicitConversions", "-language:postfixOps", "-language:higherKinds")

javaOptions +="-Duser.timezone=GMT"

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.17.0" % Test,
  "org.scalanlp" %% "breeze" % "2.1.0" % Test,
)

scalacOptions ++= {
  Seq(
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:implicitConversions",
    // disabled during the migration
    // "-Xfatal-warnings"
  ) ++
    (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _)) => Seq(
        "-unchecked",
        "-source:3.0-migration"
      )
      case _ => Seq(
        "-deprecation",
        "-Xfatal-warnings",
        "-Wunused:imports,privates,locals",
        "-Wvalue-discard"
      )
    })
}

//addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2"  cross CrossVersion.full)

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

publishTo := sonatypePublishToBundle.value