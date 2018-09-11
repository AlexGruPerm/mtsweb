package models

import play.api.Logger

import scala.collection.JavaConverters

case class PatterSearchCommResultsOneRow (
                                           ticker_id       :Int,
                                           ticker_code     :String,
                                           ticker_first    :String,
                                           ticker_seconds  :String,
                                           bar_width_sec   :Int,
                                           bars_max_ts_end :Long,
                                           patt_ts_begin   :Long,
                                           patt_ts_end     :Long,
                                           patt_end_c      :Double,
                                           ft_log_sum_u    :Double,
                                           ft_log_sum_d    :Double,
                                           ft_log_sum_n    :Double
                                         )

case class PatterSearchCommResults (
                                    allRows :Seq[PatterSearchCommResultsOneRow]
                                   )

object PatterSearchCommResults {

  def apply(cassPrepStmts: CassPreparedStmt) = {
    Logger.info("Companion object PatterSearchCommResults, method apply")
   val tickersDS :Seq[Tickers] = JavaConverters.asScalaIteratorConverter(cassPrepStmts.session.execute(
                                                  cassPrepStmts.prepTickers.bind()
                                                ).all().iterator()).asScala.toSeq
                                                 .map(row => new Tickers(
                                                   row.getInt("ticker_id"),
                                                   row.getString("ticker_code"),
                                                   row.getString("ticker_first"),
                                                   row.getString("ticker_seconds")
                                                  )).toList
    Logger.info("Companion object PatterSearchCommResults, tickersDS.size="+tickersDS.size)
    val barsPropertyDS : Seq[BarsProperty] = JavaConverters.asScalaIteratorConverter(cassPrepStmts.session.execute(
                                                              cassPrepStmts.prepBarsProperty.bind()
                                                            ).all().iterator()).asScala.toSeq
                                                             .map(row => new BarsProperty(
                                                                row.getInt("ticker_id"),
                                                                row.getInt("bar_width_sec"),
                                                                cassPrepStmts
                                                             )).toList

    val pattSearchLastsDS : Seq[PatterSearchCommResultsOneRow] =
                                                   for (
                                                        ticker       <- tickersDS;
                                                        barProperty  <- barsPropertyDS.filter(bp => bp.tickerId == ticker.ticker_id);
                                                        patSearchRes = PattSearchLasts(barProperty.tickerId,
                                                                                       barProperty.barWidthSec,
                                                                                       cassPrepStmts)
                                                       ) yield {
                                                                new PatterSearchCommResultsOneRow(
                                                                  ticker.ticker_id,
                                                                  ticker.ticker_code,
                                                                  ticker.ticker_first,
                                                                  ticker.ticker_seconds,
                                                                  barProperty.barWidthSec,
                                                                  barProperty.bars_max_ts_end,
                                                                  patSearchRes.patt_ts_begin,
                                                                  patSearchRes.patt_ts_end,
                                                                  patSearchRes.patt_end_c,
                                                                  patSearchRes.ft_log_sum_u,
                                                                  patSearchRes.ft_log_sum_d,
                                                                  patSearchRes.ft_log_sum_n
                                                                )
                                                   }
    //pattSearchLastsDS
    new PatterSearchCommResults(pattSearchLastsDS)
  }

}



