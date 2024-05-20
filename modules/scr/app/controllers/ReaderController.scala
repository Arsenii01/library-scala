package controllers

import auth.Authorization
import com.google.inject.Inject
import models.dao.entities.Reader
import models.dto.ReaderDto
import models.services.ReaderService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success}

class ReaderController @Inject()(val readerService: ReaderService) extends Authorization{

  def findAll = authorizeAdmin {
    Ok(Json.toJson(readerService.findAll))
  }


  def findById(id: String) = authorizeAdmin {
    readerService.findById(id) match {
      case Success(reader) =>
        val readerBooks = readerService.listBooks(id) match {
          case Success(books) => books
          case Failure(exception) => Nil
        }
        Ok(Json.toJson(ReaderDto.toReaderDto(reader, readerBooks)))
      case Failure(exception) => BadRequest(exception.getMessage)
    }
  }

  def insert = authorizeAdmin(parse.json[Reader]) { req =>
    val body = req.request.body
    readerService.insert(body) match {
      case Success(reader) => Ok(Json.toJson(reader))
      case Failure(ex) => BadRequest(ex.getMessage)
    }
  }

  def delete(id: String) = authorizeAdmin {
    readerService.delete(id)
    Ok
  }

  def update = authorizeAdmin(parse.json[Reader]) { req =>
    val updatedReader = req.request.body
    readerService.update(updatedReader) match {
      case Success(reader) => Ok(Json.toJson(reader))
      case Failure(ex) => BadRequest(ex.getMessage)
    }
  }

}

