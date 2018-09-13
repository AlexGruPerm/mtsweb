package models

import scala.collection.JavaConverters

case class PattSearchLasts(ticker_id       :Int,
                           bar_width_sec   :Int,
                           patt_bars_count :Int,
                           patt_ts_begin   :Long,
                           patt_ts_end     :Long,
                           patt_end_c      :Double,
                           ft_log_sum_u    :Double,
                           ft_log_sum_d    :Double,
                           ft_log_sum_n    :Double)

object PattSearchLasts{
  def apply(tickerId :Int, barWidthSec :Int, cassPrepStmts: CassPreparedStmt) = {
    val extractedPattSearchLasts :PattSearchLasts = JavaConverters.asScalaIteratorConverter(
                                                      cassPrepStmts.session.execute(
                                                        cassPrepStmts.prepPattSearchLast.bind()
                                                                     .setInt("tickerId",tickerId)
                                                                     .setInt("barWidth",barWidthSec)
                                                      ).all().iterator()
                                                   ).asScala.toSeq.map(row => new PattSearchLasts(
                                                       row.getInt("ticker_id"),
                                                       row.getInt("bar_width_sec"),
                                                       row.getInt("patt_bars_count"),
                                                       row.getLong("patt_ts_begin"),
                                                       row.getLong("patt_ts_end"),
                                                       row.getDouble("patt_end_c"),
                                                       row.getInt("ft_log_sum_u"),
                                                       row.getInt("ft_log_sum_d"),
                                                       row.getInt("ft_log_sum_n")
                                                    )).toList.head
    extractedPattSearchLasts
  }
}


