import java.sql.*
import scala.collection.mutable.ListBuffer

// Parse ResultSet
def parse_rs(rs: ResultSet): (List[String], List[List[String]]) = {

  val rsMeta = rs.getMetaData()
  val n = rsMeta.getColumnCount
  val columnNames = (1 to n).map(rsMeta.getColumnName(_))

  val rows = new ListBuffer[List[String]]
  while (rs.next()) {

    if (n > 0) {
      rows += (1 to n)
        .map(i => {
          val v = rs.getString(i)
          if v == null then "" else v // fill null value
        })
        .toList
    }
  }
  return (columnNames.toList, rows.toList)
}
