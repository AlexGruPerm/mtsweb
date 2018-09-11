package models

import scala.collection.JavaConverters

case class BarsProperty(tickerId :Int, barWidthSec :Int, cassPrepStmts: CassPreparedStmt){
    val bars_max_ts_end :Long = JavaConverters.asScalaIteratorConverter(
                                        cassPrepStmts.session.execute(cassPrepStmts.prepBarsMaxTsEnd.bind()
                                                                      .setInt("tickerId",tickerId)
                                                                      .setInt("barWidth",barWidthSec)
                                                                     ).all().iterator()).asScala.toSeq
                                                                      .map(r => r.getLong("bars_max_ts_end"))
                                                                      .toList.headOption.getOrElse(0.toLong)


}

