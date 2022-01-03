package gay.vaskel.common.models

import gay.vaskel.common.models.tables.ConfigTable
import org.jetbrains.exposed.sql.ResultRow

data class GuildConfiguration(
    val id: Long,
    val prefix: String
) {
    constructor(row: ResultRow) : this(
        row[ConfigTable.id],
        row[ConfigTable.prefix]
    )
}