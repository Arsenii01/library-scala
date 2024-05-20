package auth

import com.google.inject.Inject
import models.dao.entities.{Role, Token, User}
import models.dto.AuthDto
import play.api.libs.Crypto

import java.util.UUID
import scala.util.{Failure, Success, Try}

trait AuthorizationService {
  def check(token: Option[String]): Boolean
  def checkSuperAdmin(token: Option[String]): Boolean
  def checkAdmin(token: Option[String]): Boolean
  def login(authDto: AuthDto): Option[Token]
  def logout(token: String): Try[String]
  def registerAdmin(registerDto: AuthDto): Option[User]
  def registerUser(registerDto: AuthDto): Option[User]
}

class AuthorizationServiceImpl extends AuthorizationService{
  val authRepository = new AuthRepositoryImpl
  override def login(authDto: AuthDto): Option[Token] = {
    val user = authRepository.getUserByUsername(authDto.username)
    user match {
      case Some(u) =>
        if (u.password == Crypto.encryptAES(authDto.password)) {
          authRepository.getTokenByUserId(u.id) match {
            case Some(t) => Some(t)
            case _ =>
              val token = generateToken(authDto, u.id)
              authRepository.insert(token)
              Some(token)
          }
        } else {
          None
        }
      case _ => None
    }
  }

  override def logout(tokenBearer: String): Try[String] = {
    val token = tokenBearer.substring(7)
    authRepository.findToken(token) match {
      case Some(t) =>
        authRepository.deleteToken(token)
        Success("logout successfull")
      case _ => Failure(new Exception("token not found"))
    }
  }



  override def check(token: Option[String]): Boolean = {
    token match {
      case Some(t) if t.length > 8 => authRepository.findToken(t.substring(7)) match {
        case Some(_) => true
        case _ => false
      }
      case _ => false
    }
  }

  override def checkAdmin(token: Option[String]): Boolean = {
    token match {
      case Some(t) if t.length > 8 => authRepository.findToken(t.substring(7)) match {
        case Some(token) =>
          val role = authRepository.findById(token.id).get.role
          if (role == Role.ADMIN || role == Role.SUPERADMIN) true
          else false
        case _ => false
      }
      case _ => false
    }
  }
  override def checkSuperAdmin(token: Option[String]): Boolean = {
    token match {
      case Some(t) if t.length > 8 => authRepository.findToken(t.substring(7)) match {
        case Some(token) =>
          if (authRepository.findById(token.id).get.role == Role.SUPERADMIN) true
          else false
        case _ => false
      }
      case _ => false
    }
  }

  override def registerAdmin(registerDto: AuthDto): Option[User] = {
    authRepository.getUserByUsername(registerDto.username) match {
      case Some(u) => None
      case _ =>
        val user = User(UUID.randomUUID().toString, registerDto.username, Crypto.encryptAES(registerDto.password), Role.ADMIN)
        authRepository.insertAdmin(user)
        val token = generateToken(registerDto, user.id)
        authRepository.insert(token)
        Some(user)
    }

  }

  private def generateToken(registerDto: AuthDto, userId: String) = {
    Token(userId, Crypto.encryptAES(registerDto.username + registerDto.password + System.currentTimeMillis().toString))
  }


  override def registerUser(registerDto: AuthDto): Option[User] = {
    authRepository.getUserByUsername(registerDto.username) match {
      case Some(u) => None
      case _ =>
        val user = User(UUID.randomUUID().toString, registerDto.username, Crypto.encryptAES(registerDto.password), Role.USER)
        authRepository.insert(user)
        val token = generateToken(registerDto, user.id)
        authRepository.insert(token)
        Some(user)
    }
  }
}