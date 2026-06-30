package com.study.readwritesplitting.support

import org.testcontainers.containers.Network
import org.testcontainers.mysql.MySQLContainer
import java.sql.DriverManager

object MySqlReplicationContainers {
  private const val IMAGE = "mysql:8.4"
  private const val DB = "testdb"
  private const val ROOT_PW = "111111"
  private const val MASTER_ALIAS = "mysql-master"

  private val network: Network = Network.newNetwork()

  val master: MySQLContainer =
    MySQLContainer(IMAGE)
      .withDatabaseName(DB)
      .withPassword(ROOT_PW)
      .withNetwork(network)
      .withNetworkAliases(MASTER_ALIAS)
      .withCommand(
        "--server-id=1",
        "--log-bin=mysql-bin",
        "--gtid-mode=ON",
        "--enforce-gtid-consistency=ON",
        "--binlog-format=ROW",
      )

  val replica: MySQLContainer =
    MySQLContainer(IMAGE)
      .withDatabaseName(DB)
      .withPassword(ROOT_PW)
      .withNetwork(network)
      .withCommand(
        "--server-id=2",
        "--gtid-mode=ON",
        "--enforce-gtid-consistency=ON",
      )

  init {
    master.start()
    replica.start()
    bootstrapReplication()
  }

  fun masterHostPort(): String = "${master.host}:${master.getMappedPort(3306)}"

  fun replicaHostPort(): String = "${replica.host}:${replica.getMappedPort(3306)}"

  private fun bootstrapReplication() {
    runSql(
      master,
      "CREATE USER 'repl'@'%' IDENTIFIED BY 'repl_pass'",
      "GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%'",
      "FLUSH PRIVILEGES",
    )

    runSql(
      replica,
      """
      CHANGE REPLICATION SOURCE TO
        SOURCE_HOST='$MASTER_ALIAS',
        SOURCE_PORT=3306,
        SOURCE_USER='repl',
        SOURCE_PASSWORD='repl_pass',
        SOURCE_AUTO_POSITION=1,
        GET_SOURCE_PUBLIC_KEY=1
      """.trimIndent(),
      "START REPLICA",
      "SET GLOBAL super_read_only = ON",
    )

    awaitReplicaRunning()
  }

  private fun runSql(
    container: MySQLContainer,
    vararg statements: String,
  ) {
    DriverManager.getConnection(container.jdbcUrl, "root", ROOT_PW).use { conn ->
      conn.createStatement().use { stmt ->
        statements.forEach { stmt.execute(it) }
      }
    }
  }

  private fun awaitReplicaRunning() {
    val deadline = System.nanoTime() + 30_000_000_000L
    var last = ""
    while (System.nanoTime() < deadline) {
      DriverManager.getConnection(replica.jdbcUrl, "root", ROOT_PW).use { conn ->
        conn.createStatement().use { stmt ->
          stmt.executeQuery("SHOW REPLICA STATUS").use { rs ->
            if (rs.next()) {
              val io = rs.getString("Replica_IO_Running")
              val sql = rs.getString("Replica_SQL_Running")
              last = "IO=$io SQL=$sql lastError=${rs.getString("Last_Error")}"
              if (io == "Yes" && sql == "Yes") return
            }
          }
        }
      }
      Thread.sleep(500)
    }
    error("Replica replication threads did not start within 30s: $last")
  }
}
