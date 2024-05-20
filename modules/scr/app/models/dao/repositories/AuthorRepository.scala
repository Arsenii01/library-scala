package models.dao.repositories

import models.dao.entities.{Author, Book}
import models.dao.schema.LibrarySchema
import models.dto.AuthorDto
import org.squeryl.Table

trait AuthorRepository extends CrudRepository[String, Author]  {
  def listAllBooks(author: Author): List[Book]
}

class AuthorRepositoryImpl extends AuthorRepository {
  val authors = LibrarySchema.authors
  import org.squeryl.PrimitiveTypeMode._
  override def defaultTable: Table[Author] = LibrarySchema.authors

  override def listAllBooks(author: Author): List[Book] =
    inTransaction(author.books.toList)

}
