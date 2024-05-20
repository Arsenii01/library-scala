package module

import auth.{AuthorizationService, AuthorizationServiceImpl}
import di.AppModule
import models.dao.repositories.{AuthorRepository, AuthorRepositoryImpl, BookRepository, BookRepositoryImpl, PublisherRepository, PublisherRepositoryImpl, ReaderRepository, ReaderRepositoryImpl}

class ScrModule extends AppModule{
  override def configure(): Unit = {
    bindSingleton[AuthorRepository, AuthorRepositoryImpl]
    bindSingleton[PublisherRepository, PublisherRepositoryImpl]
    bindSingleton[ReaderRepository, ReaderRepositoryImpl]
    bindSingleton[BookRepository, BookRepositoryImpl]
    bindSingleton[AuthorRepository, AuthorRepositoryImpl]
    bindSingleton[AuthorizationService, AuthorizationServiceImpl]
  }
}
