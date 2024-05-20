package models.dao.repositories

import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.{KeyedEntity, PrimitiveTypeMode, Table}

trait CrudRepository[K, Entity <: KeyedEntity[K]] extends PrimitiveTypeMode{
  def defaultTable: Table[Entity]

  def findAll(): List[Entity] = transaction(
    from(defaultTable)(e => select(e)).toList
  )
  def findById(id: K): Option[Entity] = transaction(defaultTable.lookup(id))
  def insert(entity: Entity): Unit = transaction(defaultTable.insert(entity))
  def update(entity: Entity): Unit = transaction(defaultTable.update(entity))
  def delete(id: K): Unit = transaction(defaultTable.delete(id))

}
