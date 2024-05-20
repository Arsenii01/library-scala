package models.dao.entities

import models.dao.schema.LibrarySchema
import org.squeryl.KeyedEntity
import play.api.libs.json.Json

case class Publisher(id: String, title: String) extends KeyedEntity[String] {
  lazy val books = LibrarySchema.publisherToBooks.left(this)
}

object Publisher {
  implicit val reads = Json.reads[Publisher]
  implicit val writes = Json.writes[Publisher]
}
