package auth


import controllers.Assets.Forbidden
import play.api.libs.Crypto
import play.api.mvc.{ActionBuilder, ActionFilter, ActionTransformer, Controller, Request}

import scala.concurrent.Future

object RCAction extends ActionBuilder[RequestContext]
  with ActionTransformer[Request, RequestContext] {
  override protected def transform[A](request: Request[A]): Future[RequestContext[A]] =
    Future.successful(RequestContext[A](request, request.headers.get("Authorization")))
}

object PermissionCheckAction extends ActionFilter[RequestContext]{

  def authorizationService: AuthorizationService = new AuthorizationServiceImpl()

  def filter[A](in: RequestContext[A]) = Future.successful{
    if(!authorizationService.check(in.token)) Some(Forbidden)
    else None
  }
}

object PermissionCheckAdminAction extends ActionFilter[RequestContext]{

  def authorizationService: AuthorizationService = new AuthorizationServiceImpl()

  def filter[A](in: RequestContext[A]) = Future.successful{
    if(!authorizationService.checkAdmin(in.token)) Some(Forbidden)
    else None
  }
}

object PermissionCheckSuperAdminAction extends ActionFilter[RequestContext]{

  def authorizationService: AuthorizationService = new AuthorizationServiceImpl()

  def filter[A](in: RequestContext[A]) = Future.successful{
    if(!authorizationService.checkSuperAdmin(in.token)) Some(Forbidden)
    else None
  }
}

trait Authorization extends Controller{
  def authorize: ActionBuilder[RequestContext] = RCAction andThen PermissionCheckAction

  def authorizeSuperAdmin: ActionBuilder[RequestContext] = RCAction andThen PermissionCheckSuperAdminAction
  def authorizeAdmin: ActionBuilder[RequestContext] = RCAction andThen PermissionCheckAdminAction
}
