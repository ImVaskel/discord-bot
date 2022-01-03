package gay.vaskel.common.models.tables

import org.jetbrains.exposed.sql.*

object ConfigTable: Table("config") {
    val id = long("id")
    val prefix = varchar("prefix", 20)

    override val primaryKey = PrimaryKey(id)
}