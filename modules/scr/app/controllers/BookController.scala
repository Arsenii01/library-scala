package controllers

import auth.Authorization
import com.google.inject.Inject
import models.dao.entities.{Author, Book, Publisher, Reader}
import models.dto.{BookDto, BookHistoryDto, BookInfoDto, BookInsertDto, BookSetRequest}
import models.services.BookService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import java.time.LocalDateTime
import scala.util.{Failure, Success}

class BookController @Inject()(val bookService: BookService) extends Authorization{

  def findAll = authorize{
      val booksWithPublisher = bookService.getBooksWithPublisher()
      val books = convertToBookDto(booksWithPublisher)
      Ok(Json.toJson(books))
    }


  def findById(id: String) = authorizeAdmin{
    bookService.findBookById(id) match {
      case Success((book, publisher, authors, owner)) => Ok(Json.toJson(convertToBookInfoDto(book, publisher, authors, owner)))
      case Failure(e) => BadRequest(e.getMessage)
    }
  }


  def insert = authorizeAdmin(parse.json[BookInsertDto]) { reqContext =>
    bookService.insert(reqContext.request.body) match {
      case Success(book) => Ok(Json.toJson(book))
      case Failure(exception) => BadRequest(exception.getMessage)
    }
  }

  def update = authorizeAdmin(parse.json[BookInsertDto]) { reqContext =>
    bookService.update(reqContext.request.body) match {
      case Success(book) => Ok(Json.toJson(book))
      case Failure(exception) => BadRequest(exception.getMessage)
    }
  }

  def delete(id: String) = authorizeAdmin{
    bookService.delete(id)
    Ok
  }


  def setOwner(id: String) = authorizeAdmin(parse.json[BookSetRequest]) { reqContext =>
    bookService.setOwner(id, reqContext.request.body) match {
      case Success(book) => Ok
      case Failure(exception) => BadRequest(exception.getMessage)
    }
  }

  def release(id: String) = authorizeAdmin{
    bookService.release(id)
    Ok
  }

  def search(query: String) = authorize {
    if (query.length < 3) {
      BadRequest("Too short query")
    } else {
      val books = bookService.search(query)
      Ok(Json.toJson(books))
    }
  }

  def getHistory(id: String) = authorizeAdmin {
    val history = bookService.getHistory(id)
    Ok(Json.toJson(BookHistoryDto(history._1, history._2)))
  }

  def deleteAllHistory(id: String) = authorizeAdmin {
    bookService.deleteAllHistory(id)
    Ok
  }


  private def convertToBookDto(booksWithPublisher: List[(Book, Publisher)]) = {
    booksWithPublisher.map { case (book, publisher) =>
      BookDto(book.id, book.title, publisher, book.year, book.isbn, book.ownerId)
    }
  }

  private def convertToBookInfoDto(book: Book, publisher: Publisher, authors: List[Author], owner: Option[Reader]) = {
    val isExpired = book.takenAt match {
      case Some(takenAt) => LocalDateTime.now().isAfter(takenAt.toLocalDateTime.plusDays(book.returnPeriod.get))
      case None => false
    }
    BookInfoDto(book.id, book.title, publisher, book.year, book.isbn, authors, owner, book.takenAt, book.returnPeriod, isExpired)
  }
}
