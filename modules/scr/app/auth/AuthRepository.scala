package auth

import models.dao.entities.{Token, User}
import models.dao.repositories.CrudRepository
import models.dao.schema.LibrarySchema

trait AuthRepository extends CrudRepository[String, User] {
  def getUserByUsername(username: String): Option[User]

  def getTokenByUserId(userId: String): Option[Token]

  def insert(token: Token): Unit

  def findToken(token: String): Option[Token]

  def deleteToken(token: String): Unit

  def insertAdmin(user: User): Unit
}

class AuthRepositoryImpl extends AuthRepository {
  val users = LibrarySchema.users
  val tokens = LibrarySchema.tokens
  import org.squeryl.PrimitiveTypeMode._
  override def defaultTable = LibrarySchema.users

  override def getUserByUsername(username: String): Option[User] = {
    transaction(from(users)(u => where(u.username === username) select (u)).headOption)

  }
  override def getTokenByUserId(userId: String): Option[Token] = {
    transaction(from(tokens)(t => where(t.userId === userId) select(t)).headOption)
  }

  override def findToken(token: String): Option[Token] = {
    transaction(from(tokens)(t => where(t.token === token) select(t)).headOption)
  }


  override def insert(token: Token): Unit = inTransaction(tokens.insert(token))

  override def deleteToken(token: String): Unit = inTransaction(tokens.deleteWhere(t => t.token === token))

  override def insertAdmin(user: User): Unit = inTransaction(users.insert(user))
}
