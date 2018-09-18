package controllers

import models.{BarsProperty, CassPreparedStmt, Tickers}
import play.api.Logger

import scala.collection.JavaConverters

case class commonDataSets(cassPrepStmts: CassPreparedStmt) {

  val tickersDS :Seq[Tickers] = JavaConverters.asScalaIteratorConverter(cassPrepStmts.session.execute(
    cassPrepStmts.prepTickers.bind()
  ).all().iterator()).asScala.toSeq
    .map(row => new Tickers(
      row.getInt("ticker_id"),
      row.getString("ticker_code"),
      row.getString("ticker_first"),
      row.getString("ticker_seconds")
    )).toList

  Logger.info("Readed [tickres] inside commonDataSets, size="+tickersDS.size)

  val barsPropertyDS : Seq[BarsProperty] = JavaConverters.asScalaIteratorConverter(cassPrepStmts.session.execute(
    cassPrepStmts.prepBarsProperty.bind()
  ).all().iterator()).asScala.toSeq
    .map(row => new BarsProperty(
      row.getInt("ticker_id"),
      row.getInt("bar_width_sec"),
      cassPrepStmts
    )).toList

  Logger.info("Readed [barproperty] inside commonDataSets, size="+barsPropertyDS.size)

}
