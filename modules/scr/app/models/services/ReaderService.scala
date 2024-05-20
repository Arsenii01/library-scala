package models.services

import com.google.inject.Inject
import models.dao.entities.{Book, Reader}
import models.dao.repositories.ReaderRepository

import scala.util.{Failure, Success, Try}

class ReaderService @Inject()(val readerRepo: ReaderRepository) {

  def findAll = readerRepo.findAll()

  def findById(id: String): Try[Reader] = {
    readerRepo.findById(id) match {
      case Some(reader) => Success(reader)
      case None => Failure(new Throwable(s"Reader with id = ${id} not found"))
    }
  }

  def insert(reader: Reader): Try[Reader] = {
    readerRepo.findById(reader.id) match {
      case Some(reader) => Failure(new Throwable(s"Reader with id = ${reader.id} already exists"))
      case None =>
        readerRepo.insert(reader)
        Success(reader)
    }
  }


  def delete(id: String): Unit = {
    readerRepo.delete(id)

  }

  def update(reader: Reader): Try[Reader] = {
    readerRepo.findById(reader.id) match {
      case Some(r) =>
        readerRepo.update(reader)
        Success(reader)
      case None => Failure(new Throwable(s"Reader with id = ${reader.id} not exists"))
    }
  }


  def listBooks(id: String): Try[List[Book]] = {
    readerRepo.findById(id) match {
      case Some(reader) => Success(readerRepo.listBooks(reader))
      case None => Failure(new Throwable(s"Reader with id = ${id} not found"))
    }
  }
}
