package models

import play.api.Logger

import scala.collection.immutable.SortedMap

case class  tblRowPatterSearchCommResultsOneRow(
                                           ticker_id        :Int,
                                           ticker_code      :String,
                                           ticker_first     :String,
                                           ticker_seconds   :String,
                                           bar_width_sec    :Int,
                                           patt_bars_count  :Int,
                                           bars_max_ts_end  :Long,
                                           //bars_max_ddate  :java.util.Date, //ddate by bars_max_ts_end
                                           patt_ts_begin    :Long,
                                           patt_ts_end      :Long,
                                           patt_end_c       :Double,
                                           diff_sec_bmaxtsend_pattsend : Long,
                                           ft_log_sum_u     :Double,
                                           ft_log_sum_d     :Double,
                                           ft_log_sum_n     :Double
                                         )

case class       PatterSearchCommResultsOneRow(
                                                 ord_rn_byticker  :Int,
                                                 cnt_rows_bticker :Int,
                                                 ticker_id        :Int,
                                                 ticker_code      :String,
                                                 ticker_first     :String,
                                                 ticker_seconds   :String,
                                                 bar_width_sec    :Int,
                                                 patt_bars_count  :Int,
                                                 bars_max_ts_end  :Long,
                                                 //bars_max_ddate  :java.util.Date, //ddate by bars_max_ts_end
                                                 patt_ts_begin    :Long,
                                                 patt_ts_end      :Long,
                                                 patt_end_c       :Double,
                                                 diff_sec_bmaxtsend_pattsend : Long,
                                                 ft_log_sum_u     :Double,
                                                 ft_log_sum_d     :Double,
                                                 ft_log_sum_n     :Double
                                               )

case class PatterSearchCommResults (
                                    allRows :Seq[PatterSearchCommResultsOneRow]
                                   )

object PatterSearchCommResults {

  def apply(tickersDS :Seq[Tickers],barsPropertyDS : Seq[BarsProperty], cassPrepStmts: CassPreparedStmt) = {
    Logger.info("Companion object PatterSearchCommResults, method apply")

    val pattSearchLastsDS : Seq[tblRowPatterSearchCommResultsOneRow] =
                                                   for (
                                                        ticker       <- tickersDS.filter(t => (t.ticker_id <= 5));
                                                        barProperty  <- barsPropertyDS.filter(bp => bp.tickerId == ticker.ticker_id);
                                                        patSearchRes = PattSearchLasts(barProperty.tickerId,
                                                                                       barProperty.barWidthSec,
                                                                                       cassPrepStmts)
                                                       ) yield {
                                                                new tblRowPatterSearchCommResultsOneRow(
                                                                   ticker.ticker_id,
                                                                   ticker.ticker_code,
                                                                   ticker.ticker_first,
                                                                   ticker.ticker_seconds,
                                                                  barProperty.barWidthSec,
                                                                  patSearchRes.patt_bars_count,
                                                                  barProperty.bars_max_ts_end,
                                                                  patSearchRes.patt_ts_begin,
                                                                  patSearchRes.patt_ts_end,
                                                                  patSearchRes.patt_end_c,
                                                                  barProperty.bars_max_ts_end - patSearchRes.patt_ts_end,
                                                                  patSearchRes.ft_log_sum_u,
                                                                  patSearchRes.ft_log_sum_d,
                                                                  patSearchRes.ft_log_sum_n
                                                                )
                                                   }

    val groups = SortedMap((pattSearchLastsDS).groupBy(_.ticker_id).toSeq:_*)

    val res :Seq[PatterSearchCommResultsOneRow] =
                  (for(sq <- groups) yield {
                    for((elm,idx) <- sq._2.zipWithIndex) yield {
                      new PatterSearchCommResultsOneRow(
                        (idx+1),
                        sq._2.size,
                        elm.ticker_id,
                        elm.ticker_code,
                        elm.ticker_first,
                        elm.ticker_seconds,
                        elm.bar_width_sec,
                        elm.patt_bars_count,
                        elm.bars_max_ts_end,
                        elm.patt_ts_begin,
                        elm.patt_ts_end,
                        elm.patt_end_c ,
                        elm.diff_sec_bmaxtsend_pattsend ,
                        elm.ft_log_sum_u ,
                        elm.ft_log_sum_d ,
                        elm.ft_log_sum_n
                      )
                      }
                  }).flatten.toSeq

    new PatterSearchCommResults(res)
  }

}



