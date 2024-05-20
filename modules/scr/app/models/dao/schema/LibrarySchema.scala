package models.dao.schema

import models.dao.entities.{Author, Book, BookHistory, BookToAuthor, Publisher, Reader, Token, User}
import org.squeryl.Schema

object LibrarySchema extends Schema{
  import org.squeryl.PrimitiveTypeMode._

  val authors = table[Author]
  val publishers = table[Publisher]
  val readers = table[Reader]
  val books = table[Book]
  val bookHistory = table[BookHistory]

  val users = table[User]
  val tokens = table[Token]
  val bookToAuthor = manyToManyRelation(books, authors)
    .via[BookToAuthor]((b, a, bta) =>
      (bta.bookId === b.id, bta.authorId === a.id))

  val publisherToBooks = oneToManyRelation(publishers, books)
    .via((p, b) => p.id === b.publisherId)

  val readerToBooks = oneToManyRelation(readers, books)
    .via((r, b) => r.id === b.ownerId.getOrElse(null))

}
