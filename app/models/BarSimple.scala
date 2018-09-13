package models

import java.util.Calendar

case class BarSimple(
                      ts_end :Long,
                      o      :Double,
                      h      :Double,
                      l      :Double,
                      c      :Double
                    ){

  val calender = Calendar.getInstance()
  calender.setTimeInMillis(ts_end*1000)

  def getYear = {calender.get(Calendar.YEAR).toString}

  def getMonth = {calender.get(Calendar.MONTH).toString}

  def getDay = {calender.get(Calendar.DAY_OF_MONTH).toString}

}

