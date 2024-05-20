package models.dao.repositories

import models.dao.entities.{Author, Book, BookHistory, BookToAuthor, Publisher, Reader}
import models.dao.schema.LibrarySchema
import models.dao.schema.LibrarySchema.publishers
import models.dto.BookSetRequest
import org.squeryl.Table

import java.sql.Timestamp

trait BookRepository extends CrudRepository[String, Book]{

  def getAuthors(book: Book): List[Author]

  def getPublisher(book: Book): Publisher

  def getOwner(book: Book): Option[Reader]

  def setAuthor(bookId: String, authorId: String): Unit

  def deleteAllAuthors(bookId: String): Unit

  def getBooksWithPublisher: List[(Book, Publisher)]

  def setOwner(bookId: String, setRequest: BookSetRequest): Unit

  def release(bookId: String): Unit

  def search(query: String): List[Book]

  def getHistory(id: String): List[BookHistory]

  def deleteAllHistory(id: String): Unit

  def getOwnerCount(id: String): Int

  def insertBorrowLog(bookHistory: BookHistory): Unit
}



class BookRepositoryImpl extends BookRepository {

  val books = LibrarySchema.books
  val bookToAuthor = LibrarySchema.bookToAuthor
  val bookHistory = LibrarySchema.bookHistory
  import org.squeryl.PrimitiveTypeMode._
  override def defaultTable: Table[Book] = LibrarySchema.books

  override def getAuthors(book: Book): List[Author] =
    inTransaction(book.authors.toList)

  override def getPublisher(book: Book): Publisher =
    inTransaction(book.publisher.head)

  override def getOwner(book: Book): Option[Reader] =
    inTransaction(book.owner.headOption)

  override def setOwner(bookId: String, setRequest: BookSetRequest): Unit = {
    inTransaction {
      update(books)(b => where(b.id === bookId) set (b.ownerId := Some(setRequest.readerId), b.takenAt := Some(new Timestamp(System.currentTimeMillis())), b.returnPeriod := Some(setRequest.returnPeriod)))
    }
  }


  override def setAuthor(bookId: String, authorId: String): Unit =
    inTransaction {
      bookToAuthor.insert(BookToAuthor(bookId, authorId))
    }

  override def deleteAllAuthors(bookId: String): Unit =
    inTransaction {
      bookToAuthor.deleteWhere(_.bookId === bookId)
    }

  override def release(bookId: String): Unit = {
    inTransaction {
      update(books)(b => where(b.id === bookId) set (b.ownerId := None, b.takenAt := None, b.returnPeriod := None))
    }
  }

  override def getBooksWithPublisher: List[(Book, Publisher)] = {
    transaction(join(books, publishers)((b, p) => where(b.publisherId === p.id) select(b, p) on (b.publisherId === p.id)).toList)
//    inTransaction(from(books)(b => select(b, b.publisher.head)).toList)
  }

  override def search(query: String): List[Book] = {
    transaction{
      join(books, publishers)((b, p) => where(
        (lower(b.title) like s"%${query.toLowerCase}%")
        or (lower(b.isbn.orNull) like s"%${query.toLowerCase}%")
        or (lower(p.title) like s"%${query.toLowerCase}%"))
        select(b) on (b.publisherId === p.id)).toList
    }
  }


//    transaction(from(books)(b => where((b.title like s"%$query%") or (b.isbn.orNull like s"%$query%") or (b.publisher.head.title like s"%$query%")) select b).toList)

  override def getHistory(id: String): List[BookHistory] = {
    transaction(
        from(bookHistory)(b => where(b.bookId === id) select (b)).toList
      )
  }

  override def getOwnerCount(id: String): Int = {
    transaction(
      from(bookHistory)(b => where(b.bookId === id) select (count(b.readerId))).size
    )
  }

  override def insertBorrowLog(bookHistoryLog: BookHistory): Unit = {
    transaction(
      bookHistory.insert(bookHistoryLog)
    )
  }

  override def deleteAllHistory(id: String): Unit = {
    inTransaction{
      bookHistory.deleteWhere(_.bookId === id)
    }
  }
}