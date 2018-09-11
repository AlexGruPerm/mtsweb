package controllers

import javax.inject._
import models.{CassPreparedStmt, PatterSearchCommResults}
import play.api.mvc._
import play.filters.csrf.AddCSRFToken
import play.api.Logger

@Singleton
class PattSearchResController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  val client = new CassClient("127.0.0.1")
  val cassPrepStmts = new CassPreparedStmt(client.session)
  val resDS = PatterSearchCommResults(cassPrepStmts)

  Logger.info("We are here")

  @AddCSRFToken
  def showPattSearchResults = Action {
    Logger.info("PattSearchResController resDS.allRows.size="+resDS.allRows.size)
    Ok(views.html.pattsearch("XXXYYYZZZ",resDS.allRows))
  }

}

