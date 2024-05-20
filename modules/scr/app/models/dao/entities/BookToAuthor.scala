package models.dao.entities

import org.squeryl.KeyedEntity
import org.squeryl.PrimitiveTypeMode.compositeKey
import org.squeryl.dsl.CompositeKey2

case class BookToAuthor(bookId: String, authorId: String) extends KeyedEntity[CompositeKey2[String, String]] {
  override def id: CompositeKey2[String, String] = compositeKey(bookId, authorId)
}

