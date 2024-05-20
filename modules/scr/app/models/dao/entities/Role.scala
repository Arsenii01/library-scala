package models.dao.entities

import play.api.libs.json.{Format, JsResult, JsString, JsSuccess, JsValue, Json, Writes}

object Role extends Enumeration {
  type Role = Value
  val SUPERADMIN = Value(1, "SUPERADMIN")
  val ADMIN = Value(2, "ADMIN")
  val USER = Value(3, "USER")

  implicit val roleFormat = new Format[Role] {
    override def writes(o: Role): JsValue = JsString(o.toString)

    override def reads(json: JsValue) = JsSuccess(Role.withName(json.as[String]))
  }
}

