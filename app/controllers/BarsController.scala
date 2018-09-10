package controllers

import javax.inject._
import play.api.mvc._

  @Singleton
  class BarsController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
    extends AbstractController(cc) {

    def bars = Action {
      Ok(views.html.barscnt("This is a bars page."))
    }

  }


