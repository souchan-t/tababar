val ScalatraVersion = "2.7.0"

organization := "com.github.souchan_t"

name := "tababar"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.13.1"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-forms" % "2.7.0",
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.19.v20190610" % "container;compile",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.scalatra" %% "scalatra-json" % "2.7.0",
  "org.json4s" %% "json4s-jackson" % "3.6.7",
  "com.google.inject" % "guice" % "4.2.2",
  "org.springframework.security" % "spring-security-crypto" % "5.3.0.RELEASE",
  "com.h2database" % "h2" % "1.4.200",
  "org.scalikejdbc" %% "scalikejdbc" % "3.4.1",
  "com.zaxxer" % "HikariCP" % "3.4.2"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
