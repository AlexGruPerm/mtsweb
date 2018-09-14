package controllers

import javax.inject._
import models.{BarSimple, BarsReaderSimple, CassPreparedStmt, PatterSearchCommResults}
import play.api.Logger
import play.api.libs.json.{Json, Writes}
import play.api.mvc._
import play.filters.csrf.AddCSRFToken

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
    //rewrite with TABLE ON DIVS: https://html-cleaner.com/features/replace-html-table-tags-with-divs
    Ok(views.html.pattsearch("XXXYYYZZZ",resDS.allRows))
  }

  @AddCSRFToken
    def  getJsonBarsByTickerWidthDeep(tickerid: Int, barwidthsec :Int, deeplimit:Int, tsend : Long)= Action {
    val bars :Seq[BarSimple] = (new BarsReaderSimple(tickerid, barwidthsec, deeplimit, tsend, cassPrepStmts)).getBars
    implicit val residentWrites = new Writes[BarSimple] {
      def writes(bar: BarSimple) = Json.obj(
        "x" -> ("new Date("+bar.getYear+", "+bar.getMonth+", "+bar.getDay+")"),
                "y" -> (bar.o, bar.h, bar.l, bar.c)
      )
    }
    Ok(Json.toJson(bars))
  }


}

