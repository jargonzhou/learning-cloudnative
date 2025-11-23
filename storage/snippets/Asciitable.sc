// import $ivy.`de.vandermeer:asciitable:0.3.2`, de.vandermeer.asciitable._
import de.vandermeer.asciitable._

// Conversions Between Java and Scala Collections
// https://docs.scala-lang.org/overviews/collections-2.13/conversions-between-java-and-scala-collections.html
import scala.jdk.CollectionConverters.*

// def print_table(header: java.util.List[String], rows: java.util.List[java.util.List[String]]): Unit = {
def print_table(header: List[String], rows: List[List[String]]): Unit = {
  // println(header.length)
  val at = new AsciiTable()
  at.addRule()
  at.addRow(header.asJava)
  at.addRule()
  if (rows.length > 0) {
    // println(rows.head.length)
    for row <- rows do {
      at.addRow(row.asJava)
    }
  }

  at.addRule()

  // http://www.vandermeer.de/projects/skb/java/asciitable/examples/AT_07d_LongestWord.html
  at.getRenderer().setCWC(new CWC_LongestWord());
  println(at.render())
}

// import $ivy.`com.google.guava:guava:33.4.0-jre`, com.google.common.collect._
// val header = Lists.newArrayList("A", "B")
// val rows = Lists.newArrayList(Lists.newArrayList("a1", "b1"), Lists.newArrayList("a2", "b2"))

// val header = List("A", "B")
// val rows = List(List("a1", "b1"), List("a2", "b2"))
// print_table(header, rows)
