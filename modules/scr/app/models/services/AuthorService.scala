package models.services

import com.google.inject.Inject
import models.dao.entities.Author
import models.dao.repositories.AuthorRepository

import scala.util.{Failure, Success, Try}

class AuthorService @Inject()(val authorRepo: AuthorRepository){
  def findAll = authorRepo.findAll()
  def findById(id: String) = {
    authorRepo.findById(id) match {
      case Some(author) => Success(author)
      case None => Failure(new Throwable(s"Author with id = ${id} not found"))
    }
  }
  def insert(author: Author): Try[Author] = {
    authorRepo.findById(author.id) match {
      case Some(author) => Failure(new Throwable(s"Author with id = ${author.id} already exists"))
      case None =>
        authorRepo.insert(author)
        Success(author)
    }
  }

  def update(author: Author) = {
    authorRepo.findById(author.id) match {
      case Some(author) =>
        authorRepo.update(author)
        Success(author)
      case None => Failure(new Throwable(s"Author with id = ${author.id} not exists"))
    }
  }

  def delete(id: String): Unit = {
    authorRepo.delete(id)
  }

  def listBooks(id: String) = {
    authorRepo.findById(id) match {
      case Some(author) => Success(authorRepo.listAllBooks(author))
      case None => Failure(new Throwable(s"Author with id = ${id} not found"))
    }
  }

}
