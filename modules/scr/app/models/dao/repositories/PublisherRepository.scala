package models.dao.repositories

import models.dao.entities.{Book, Publisher}
import models.dao.schema.LibrarySchema
import org.squeryl.Table

trait PublisherRepository extends CrudRepository[String, Publisher]{
  def listAllBooks(publisher: Publisher): List[Book]
}

class PublisherRepositoryImpl extends PublisherRepository {
  val publishers = LibrarySchema.publishers
  val books = LibrarySchema.books

  import org.squeryl.PrimitiveTypeMode._

  override def defaultTable: Table[Publisher] = LibrarySchema.publishers

  override def listAllBooks(publisher: Publisher): List[Book] = {
    inTransaction(publisher.books.toList)
  }

}
