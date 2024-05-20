package models.dao.entities

import models.dao.entities.Role.Role
import org.squeryl.KeyedEntity
import play.api.libs.json.Json

case class User(id: String,
                username: String,
                password: String,
                role: Role) extends KeyedEntity[String] {
  def this() = this("", "", "", Role.USER)
}

object User {
  implicit val userWrites = Json.writes[User]
}
