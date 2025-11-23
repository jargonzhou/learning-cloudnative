// import $ivy.`sqlline:sqlline:1.12.0`, sqlline.SqlLine
// import $ivy.`org.apache.calcite:calcite-csv:1.38.0`

import sqlline.SqlLine;
import java.io.*;
import java.nio.charset.StandardCharsets;

import scala.collection.mutable.ListBuffer

def execCommand(dbUrl: String, command: String = "", script: String = ""): Unit = {
  val sqlline = new SqlLine()
  val baos = new ByteArrayOutputStream()
  val in = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
  val sqllineOutputStream = new PrintStream(baos, true, StandardCharsets.UTF_8.name())
  sqlline.setOutputStream(sqllineOutputStream)
  sqlline.setErrorStream(sqllineOutputStream)

  // option reference
  // https://julianhyde.github.io/sqlline/manual.html#options
  val args = new ListBuffer[String]
  args += "-u"
  args += dbUrl
  args += "-n"
  args += "admin"
  args += "-p"
  args += "admin"

  // parameter reference
  // https://julianhyde.github.io/sqlline/manual.html#settings
  args += "--maxwidth=1000"
  args += "--maxcolumnwidth=100"

  // "-log", "sqline.log",
  if (command.length() > 0) {
      args += "-e"
      args += command
  }
  if (script.length() > 0) {
    args += "--run"
    args += script
  }
  
  val status = sqlline.begin(args.toList.toArray, in, false) // MUST use in, otherwise it's SYSTEM console
  println(s"Status: ${status}")
  println(baos.toString("UTF8"))
  // return baos.toString("UTF8")
}