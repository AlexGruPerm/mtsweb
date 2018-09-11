package models

case class Task(id: Int, name: String)

object Task {

  private var taskList: List[Task] = List()

  def all: List[Task] = {
    taskList
  }

  def add(taskName: String) = {
    val newId: Int = if (taskList.isEmpty) 1 else taskList.last.id + 1
    taskList = taskList ++ List(Task(newId, taskName))
  }

  def delete(taskId: Int) = {
    taskList = taskList.filterNot(task => task.id == taskId)
  }

}
