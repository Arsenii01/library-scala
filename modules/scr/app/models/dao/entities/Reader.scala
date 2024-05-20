package models.dao.entities

import models.dao.schema.LibrarySchema
import org.squeryl.KeyedEntity
import play.api.libs.json.Json

case class Reader(id: String, name: String, email: String) extends KeyedEntity[String] {
  lazy val books = LibrarySchema.readerToBooks.left(this)
}

object Reader {
  implicit val reads = Json.reads[Reader]
  implicit val writes = Json.writes[Reader]
}
