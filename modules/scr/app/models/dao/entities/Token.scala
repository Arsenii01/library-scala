package models.dao.entities

import org.squeryl.KeyedEntity
import play.api.libs.json.Json

case class Token(userId: String, token: String) extends KeyedEntity[String] {
  override def id: String = userId
}


object Token {
  implicit val tokenReads = Json.reads[Token]
  implicit val tokenWrites = Json.writes[Token]
}