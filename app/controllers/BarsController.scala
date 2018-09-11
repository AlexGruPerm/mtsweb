package controllers

import javax.inject._
import models.Task
import play.api.Logger
import play.api.mvc._
import play.filters.csrf.{AddCSRFToken, RequireCSRFCheck}

  @Singleton
  class BarsController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
    extends AbstractController(cc) {

    @AddCSRFToken
    def bars = Action {
      Logger.info("BarsController - bars Task.all.size="+Task.all.size)
      Ok(views.html.barscnt("This is a bars page.",Task.all))
    }

    @RequireCSRFCheck
    def newTask = Action(parse.formUrlEncoded) {
      implicit request => {
        Task.add(request.body.get("taskName").get.head)
        Logger.info("request.body="+request.body)
        Logger.info("BarsController - newTask request.body.get(taskName).get.head="+request.body.get("taskName").get.head)
        Redirect(routes.BarsController.bars)
      }
    }

    def deleteTask(id: Int) = Action {
      Task.delete(id)
      Ok
    }

  }


