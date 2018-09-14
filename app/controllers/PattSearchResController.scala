package controllers

import javax.inject._
import models.{BarSimple, BarsReaderSimple, CassPreparedStmt, PatterSearchCommResults}
import play.api.Logger
import play.api.libs.json._
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

      def writes(bar: BarSimple) =
                              Json.obj(
                                "ts_end" -> bar.ts_end,
                                        "c" -> bar.c
                               )
    }

    val barsJsonArr = Json.toJson(bars)

    val jres =Json.obj(
      "tickerID" -> tickerid.toString,
      "data" -> barsJsonArr
    )

    Ok(jres)
  }

/*----------------------------------*/
/*
  val json: JsValue = Json.obj(
    "name" -> "Watership Down",
    "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197),
    "residents" -> Json.arr(
      Json.obj(
        "name" -> "Fiver",
        "age" -> 4,
        "role" -> JsNull
      ),
      Json.obj(
        "name" -> "Bigwig",
        "age" -> 6,
        "role" -> "Owsla"
      )
    )
  )

  {
    "label": "Japan",
    "data": [[1999, -0.1], [2000, 2.9], [2001, 0.2], [2002, 0.3], [2003, 1.4], [2004, 2.7], [2005, 1.9], [2006, 2.0], [2007, 2.3], [2008, -0.7]]
  }
*/
/*--------------------------------*/

}

