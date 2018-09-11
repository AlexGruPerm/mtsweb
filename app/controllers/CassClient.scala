package controllers

import com.datastax.driver.core.Cluster

//import com.datastax.driver.core.Cluster

class CassClient(node: String) {

  private val cluster = Cluster.builder().addContactPoint(node).build()
  val session = cluster.connect()

  def close() {
    session.close
    cluster.close
  }

}
