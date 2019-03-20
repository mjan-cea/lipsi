name := "Lipsi"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls")

libraryDependencies += scalaVersion("org.scala-lang" % "scala-compiler" % _).value

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

// Provide a managed dependency on X if -DXVersion="" is supplied on the command line.
// The following are the current "release" versions.
//val defaultVersions = Map(
//  "chisel3" -> "3.1.+",
//  "chisel-iotesters" -> "1.2.+"
//)

libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.1.3"
libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.2.2"
//libraryDependencies ++= (Seq("chisel3","chisel-iotesters").map {
//  dep: String => "edu.berkeley.cs" %% dep % sys.props.getOrElse(dep + "Version", defaultVersions(dep)) })
