package controllers

import auth.Authorization
import com.google.inject.Inject
import models.dao.entities.Publisher
import models.services.PublisherService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success}

class PublisherController @Inject()(val publisherService: PublisherService) extends Authorization{

  def findAll = authorize {
    Ok(Json.toJson(publisherService.findAll))
  }

  def findById(id: String) = authorizeAdmin {
    publisherService.findById(id) match {
      case Success(publisher) => Ok(Json.toJson(publisher))
      case Failure(exception) => BadRequest(exception.getMessage)
    }
  }

  def insert() = authorizeAdmin(parse.json[Publisher]) { request =>
    publisherService.insert(request.request.body) match {
      case Success(publisher) => Ok(Json.toJson(publisher))
      case Failure(exception) => BadRequest(exception.getMessage)
    }
  }

  def update() = authorizeAdmin(parse.json[Publisher]) { request =>
    publisherService.update(request.request.body) match {
      case Success(publisher) => Ok(Json.toJson(publisher))
      case Failure(exception) => BadRequest(exception.getMessage)
    }
  }

  def delete(id: String) = authorizeAdmin {
    publisherService.delete(id)
    Ok
  }

  def listBooks(id: String) = authorizeAdmin {
    publisherService.listBooks(id) match {
      case Success(books) => Ok(Json.toJson(books))
      case Failure(exception) => BadRequest(exception.getMessage)
    }
  }



}
