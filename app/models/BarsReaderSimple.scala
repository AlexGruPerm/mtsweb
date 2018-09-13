package models

import com.datastax.driver.core.LocalDate

import scala.collection.JavaConverters

class BarsReaderSimple(ticker_id :Int, bar_width_sec :Int, deepLimit :Int, currTsEnd: Long, cassPrepStmts: CassPreparedStmt) {

    val lc :LocalDate =
      cassPrepStmts.session.execute(
        cassPrepStmts.prepBarsDateByCurrTsend.bind()
          .setInt("tickerId",ticker_id)
          .setInt("barWidth",bar_width_sec)
          .setLong("currtsend",currTsEnd)
      ).one().getDate("ddate")


  val bars : Seq[BarSimple] = JavaConverters.asScalaIteratorConverter(cassPrepStmts.session.execute(
    cassPrepStmts.prepBarsWithDeep.bind()
      .setInt("tickerId",ticker_id)
      .setInt("barWidth",bar_width_sec)
      .setDate("pddate", lc)
      .setInt("deepLimit",deepLimit)).all().iterator()).asScala.toSeq
    .map(row => new BarSimple(
      row.getLong("ts_end"),
      row.getDouble("o"),
      row.getDouble("h"),
      row.getDouble("l"),
      row.getDouble("c")
    )).toList

  def getBars = {bars}

}

