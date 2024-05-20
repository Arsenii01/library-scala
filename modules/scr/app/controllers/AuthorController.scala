package controllers

import auth.Authorization
import com.google.inject.Inject
import models.dao.entities.Author
import models.services.AuthorService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success}

class AuthorController @Inject()(val authorService: AuthorService)  extends Authorization {

  def findAll = authorize {
      Ok(Json.toJson(authorService.findAll))
    }
  def insert = authorizeAdmin(parse.json[Author]) { req =>
    val body = req.request.body
    authorService.insert(body) match {
      case Success(author) => Ok(Json.toJson(author))
      case Failure(ex) => BadRequest(ex.getMessage)
    }

  }
  def delete(id: String) = authorizeAdmin {
    authorService.delete(id)
    Ok
  }

  def update = authorizeAdmin(parse.json[Author]) { req =>
    val updatedAuthor = req.request.body
    authorService.update(updatedAuthor) match {
      case Success(author) => Ok(Json.toJson(author))
      case Failure(ex) => BadRequest(ex.getMessage)
    }
  }

  def listAuthorBooks(id: String) = authorizeAdmin {
    authorService.listBooks(id) match {
      case Success(books) => Ok(Json.toJson(books))
      case Failure(ex) => BadRequest(ex.getMessage)
    }
  }
}
