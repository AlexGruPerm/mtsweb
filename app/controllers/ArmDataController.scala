package controllers

import anorm.{RowParser, SQL, SqlParser}
import javax.inject._
import play.api.Logger
import play.api.db.Database
import play.api.mvc._
import play.filters.csrf.AddCSRFToken

//import play.api.db._

//class ScalaControllerInject @Inject()(db: Database, val controllerComponents: ControllerComponents) extends BaseController {

/*
import javax.inject._
@Singleton
extends Controller
db: Database,
*/

class ArmDataController @Inject()(db: Database, val controllerComponents: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends BaseController {

  /*
  val client = new CassClient("10.241.5.234")
  val cassPrepStmts = new CassPreparedStmt(client.session)
  val commDS = commonDataSets(cassPrepStmts)
 */

  case class ArmDataIR(
                        ID_UK     :String,
                        N_ORDER   :String,
                        ID_POK    :String,
                        ID_ROW    :String,
                        VAL_2     :String,
                        VAL_6     :String,
                        VAL_11    :String,
                        VAL_13    :String,
                        VAL_14    :String,
                        VAL_21    :String,
                        VAL_22    :String,
                        VAL_70    :String,
                        VAL_80    :String,
                        VAL_17    :String,
                        VAL_18    :String,
                        VAL_19    :String,
                        VAL_20    :String,
                        VAL_30    :String,
                        VAL_74    :String,
                        VAL_75    :String,
                        VAL_76    :String,
                        VAL_77    :String,
                        VAL_78    :String,
                        VAL_79    :String,
                        VAL_81    :String,
                        VAL_82    :String,
                        VAL_31    :String,
                        VAL_32    :String,
                        VAL_7     :String,
                        VAL_9     :String,
                        VAL_16    :String,
                        VAL_29    :String,
                        VAL_36    :String,
                        VAL_83    :String,
                        VAL_33    :String,
                        VAL_34    :String,
                        VAL_73    :String
                      )

  Logger.info("Instance ArmDataController Singleton")

  def getSliceTabData = db.withConnection { implicit connection =>

    val rowParser: RowParser[Map[String,Any]] =
      SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
        Right(map + (meta.column.qualified -> value))
      }
/*
    val contextDB  =
      SQL("EXEC PKG_ARM_STRUCT.set_context_variables(37159, 3, 15, 20181201, 20171201, 9, 5, 300 , null)")
        .withResultSetOnFirstRow(true).executeQuery()
    */

      SQL("EXEC PKG_ARM_STRUCT.set_context_variables(37159,3,15,20181201,20171201,9,5,300,null)")
        .executeQuery()

    val parsedRes:  List[Map[String, Any]] = SQL("select pkg_arm_data.f_get_data from dual").as(rowParser.*)
  }


  @AddCSRFToken
    def  getData = Action {implicit connection =>
    Logger.info("ArmDataController.getData")
    val conn = db.getConnection()
    var outString = "Number is "

    try {
      Logger.info("ArmDataController.getData TRY QUERY ORACLE")
      outString += "res manual"

      val pRes = getSliceTabData
      /*
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT 101213 as testkey from dual")
      while (rs.next()) {
        outString += rs.getString("testkey")
      }
      */


    } finally {
      conn.close()
    }

    Ok(outString)

    /*
    val jres: JsValue = Json.obj(
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
    Ok(jres)

    */

  }


}

