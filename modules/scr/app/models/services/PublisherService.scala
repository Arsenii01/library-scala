package models.services

import com.google.inject.Inject
import models.dao.entities.Publisher
import models.dao.repositories.PublisherRepository

import scala.util.{Failure, Success, Try}

class PublisherService @Inject()(val publisherRepo: PublisherRepository) {

  def findAll = publisherRepo.findAll()

  def findById(id: String) = {
    publisherRepo.findById(id) match {
      case Some(publisher) => Success(publisher)
      case None => Failure(new Throwable(s"Publisher with id = ${id} not found"))
    }
  }

  def insert(publisher: Publisher): Try[Publisher] = {
    publisherRepo.findById(publisher.id) match {
      case Some(publisher) => Failure(new Throwable(s"Publisher with id = ${publisher.id} already exists"))
      case None =>
        publisherRepo.insert(publisher)
        Success(publisher)
    }
  }

  def update(publisher: Publisher) = {
    publisherRepo.findById(publisher.id) match {
      case Some(p) =>
        publisherRepo.update(publisher)
        Success(publisher)
      case None => Failure(new Throwable(s"Publisher with id = ${publisher.id} not exists"))
    }
  }

  def delete(id: String): Unit = {
    publisherRepo.delete(id)
  }

  def listBooks(id: String) = {
    publisherRepo.findById(id) match {
      case Some(publisher) => Success(publisherRepo.listAllBooks(publisher))
      case None => Failure(new Throwable(s"Publisher with id = ${id} not found"))
    }
//    publisherRepo.listAllBooks(id) match {
//      case List(books) => Success(books)
//      case _ => Failure(new Throwable(s"Publisher with id = ${id} not found"))
//    }
  }

}
