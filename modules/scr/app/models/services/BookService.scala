package models.services

import com.google.inject.Inject
import models.dao.entities.{Author, Book, BookHistory, Publisher, Reader}
import models.dao.repositories.{BookRepository, ReaderRepository}
import models.dto.{BookDto, BookInsertDto, BookSetRequest}

import java.sql.Timestamp
import java.util.UUID
import scala.util.{Failure, Success, Try}

class BookService @Inject()(val bookRepo: BookRepository,
                            val readerRepository: ReaderRepository) {

  def findAll = {
    bookRepo.findAll()
  }

  def getBooksWithPublisher() = {
    bookRepo.getBooksWithPublisher
  }


  def findById(id: String) = {
    bookRepo.findById(id) match {
      case Some(book) => Success(book)
      case None => Failure(new Throwable(s"Book with id = ${id} not found"))
    }
  }

  def search(query: String): List[Book] = {
    bookRepo.search(query)
  }

  def insert(bookDto: BookInsertDto): Try[Book] = {
    bookRepo.findById(bookDto.id) match {
      case Some(book) => Failure(new Throwable(s"Book with id = ${bookDto.id} already exists"))
      case None =>
        val book = convertToBook(bookDto)
        bookRepo.insert(book)
        bookDto.authors.foreach(a =>
          bookRepo.setAuthor(book.id, a)
        )
        Success(book)
    }
  }

  def update(bookDto: BookInsertDto): Try[Book] = {
    bookRepo.findById(bookDto.id) match {
      case Some(b) =>
        val book = convertToBook(bookDto)
        bookRepo.deleteAllAuthors(book.id)
        bookRepo.update(book)
        bookDto.authors.foreach(a =>
          bookRepo.setAuthor(book.id, a)
        )
        Success(book)
      case None => Failure(new Throwable(s"Book with id = ${bookDto.id} not exists"))
    }
  }


  def delete(id: String): Unit = {
    bookRepo.delete(id)
  }

  def getHistory(id: String): (List[BookHistory], Int) = {
    val history = bookRepo.getHistory(id)
    val count = bookRepo.getOwnerCount(id)
    (history, count)
  }

  def deleteAllHistory(id: String): Unit = {
    bookRepo.deleteAllHistory(id)
  }

  def findBookById(id: String) : Try[(Book, Publisher, List[Author], Option[Reader])] = {
    bookRepo.findById(id) match {
      case Some(book) =>
        Success((book, bookRepo.getPublisher(book), bookRepo.getAuthors(book), bookRepo.getOwner(book)))
      case None => Failure(new Throwable(s"Book with id = ${id} not found"))
    }
  }

  def getPublisher(book: Book) = {
    bookRepo.getPublisher(book)
  }

  def setOwner(bookId: String, bookSetRequest: BookSetRequest) = {
    readerRepository.findById(bookSetRequest.readerId) match {
      case Some(reader) =>
        val book = bookRepo.findById(bookId)
        if (book.isEmpty || book.get.ownerId.nonEmpty) {
          Failure(new Throwable(s"Книга с id = ${bookId} уже выдана или не существует"))
        } else {
          bookRepo.setOwner(bookId, bookSetRequest)
          Success("Книга успешно выдана")
        }
      case None => Failure(new Throwable(s"Reader with id = ${bookSetRequest.readerId} not found"))
    }
  }

  def release(bookId: String): Unit = {
    val bookOption = bookRepo.findById(bookId)
    bookOption match {
      case Some(book) =>
        val bookBorrowLog = BookHistory(UUID.randomUUID().toString, book.id, book.ownerId.get, book.takenAt.get, new Timestamp(System.currentTimeMillis()))
        bookRepo.release(bookId)
        bookRepo.insertBorrowLog(bookBorrowLog)
      case None => throw new Throwable(s"Book with id = ${bookId} not found")
    }
  }

  private def convertToBook(bookInsertDto: BookInsertDto) = {
    Book(bookInsertDto.id, bookInsertDto.title, bookInsertDto.publisherId, bookInsertDto.year, bookInsertDto.isbn, None, None, None)
  }

//  def getPublisher(id: String) = {
//    bookRepo.findById(id) match {
//      case Some(book) => Success(bookRepo.getPublisher(book))
//      case None => Failure(new Throwable(s"Book with id = ${id} not found"))
//    }
//  }
//
//  def getOwner(id: String) = {
//    bookRepo.findById(id) match {
//      case Some(book) => Success(bookRepo.getOwner(book))
//      case None => Failure(new Throwable(s"Book with id = ${id} not found"))
//    }
//  }
//
//  def listAuthors(id: String) = {
//    bookRepo.findById(id) match {
//      case Some(book) => Success(bookRepo.getAuthors(book))
//      case None => Failure(new Throwable(s"Book with id = ${id} not found"))
//    }
//  }

}
