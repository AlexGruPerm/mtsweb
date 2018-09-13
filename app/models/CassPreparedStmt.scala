package models

import com.datastax.driver.core.Session

case class CassPreparedStmt(session : Session) {

  val prepTickers = session.prepare(""" select * from mts_meta.tickers """)

  val prepBarsProperty = session.prepare(""" select * from mts_meta.bars_property """)

  val prepBarsMaxTsEnd = session.prepare(""" select max(ts_end) as bars_max_ts_end
                                                       from mts_bars.bars
                                                      where ticker_id     = :tickerId and
                                                            bar_width_sec = :barWidth
                                                      allow filtering """)

  val prepPattSearchLast = session.prepare(""" select ticker_id,bar_width_sec,patt_bars_count,patt_ts_begin,
                                                              max(patt_ts_end) as patt_ts_end,patt_end_c,
                                                              ft_log_sum_u, ft_log_sum_d, ft_log_sum_n
                                                         from mts_bars.pattern_search_results
                                                        where ticker_id     = :tickerId and
                                                              bar_width_sec = :barWidth """)

  val prepBarsDateByCurrTsend = session.prepare(""" select ddate
                                                             from mts_bars.bars
                                                            where ticker_id     = :tickerId and
                                                                  bar_width_sec = :barWidth and
                                                                  ts_end        = :currtsend
                                                            limit 1 allow filtering """)

  val prepBarsWithDeep = session.prepare(""" select ts_end,o,h,l,c
                                                       from mts_bars.bars
                                                      where ticker_id     = :tickerId and
                                                            bar_width_sec = :barWidth and
                                                            ddate         = :pddate
                                                      limit :deepLimit
                                                      allow filtering """)

}
