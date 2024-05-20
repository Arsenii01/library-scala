package models.dao.repositories

import models.dao.entities.{Book, Reader}
import models.dao.schema.LibrarySchema
import org.squeryl.Table

trait ReaderRepository extends CrudRepository[String, Reader]{

  def listBooks(reader: Reader): List[Book]
}

class ReaderRepositoryImpl extends ReaderRepository {

  val readers = LibrarySchema.readers
  import org.squeryl.PrimitiveTypeMode._
  override def defaultTable: Table[Reader] = LibrarySchema.readers


  override def listBooks(reader: Reader): List[Book] = {
    inTransaction(reader.books.toList)
  }
}
