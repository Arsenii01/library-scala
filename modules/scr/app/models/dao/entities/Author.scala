package models.dao.entities

import models.dao.schema.LibrarySchema
import org.squeryl.KeyedEntity
import play.api.libs.json.Json

case class Author(id: String, name: String) extends KeyedEntity[String] {
  lazy val books = LibrarySchema.bookToAuthor.right(this)
//  def this(name: String) = this(0, name)
}

object Author {
  implicit val reads = Json.reads[Author]
  implicit val writes = Json.writes[Author]
}


