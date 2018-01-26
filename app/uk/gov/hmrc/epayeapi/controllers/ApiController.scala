/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.epayeapi.controllers

import akka.stream.Materializer
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.streams.Accumulator
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.{EmptyRetrieval, Retrievals}
import uk.gov.hmrc.domain.EmpRef
import uk.gov.hmrc.epayeapi.models.Formats._
import uk.gov.hmrc.epayeapi.models.in._
import uk.gov.hmrc.epayeapi.models.out.ApiErrorJson
import uk.gov.hmrc.epayeapi.models.out.ApiErrorJson.{InsufficientEnrolments, _}
import uk.gov.hmrc.play.binders.SimpleObjectBinder
import uk.gov.hmrc.play.microservice.controller.BaseController

import scala.concurrent.{ExecutionContext, Future}

trait ApiController extends BaseController with AuthorisedFunctions {
  val epayeEnrolment = Enrolment("IR-PAYE")
  val epayeRetrieval = Retrievals.authorisedEnrolments
  def authConnector: AuthConnector
  implicit def ec: ExecutionContext
  implicit def mat: Materializer

  def AuthorisedAction(enrolment: Enrolment)(action: => EssentialAction): EssentialAction = {
    EssentialAction { implicit request =>
      Accumulator.done {
        authorised(enrolment.withDelegatedAuthRule("epaye-auth")).retrieve(EmptyRetrieval) { f =>
          action(request).run()
        } recoverWith {
          case ex: AuthorisationException =>
            Logger.error("Error authorizing user", ex)
            genericAuthorisationError(ex.reason)
        }
      }
    }
  }

  def AuthorisedEmpRefAction(empRefFromUrl: EmpRef)(action: EssentialAction): EssentialAction = {
    val enrolment = epayeEnrolment
      .withIdentifier("TaxOfficeNumber", empRefFromUrl.taxOfficeNumber)
      .withIdentifier("TaxOfficeReference", empRefFromUrl.taxOfficeReference)

    AuthorisedAction(enrolment) {
      EssentialAction { implicit request =>
        Accumulator.done(action(request).run())
      }
    }
  }

  def authorizationHeaderInvalid: Future[Result] =
    Future.successful(Unauthorized(Json.toJson(AuthorizationHeaderInvalid)))
  def insufficientEnrolments: Future[Result] =
    Future.successful(Forbidden(Json.toJson(InsufficientEnrolments)))
  def bearerTokenExpired: Future[Result] =
    Future.successful(Unauthorized(Json.toJson(TokenExpired)))
  def invalidEmpRef: Future[Result] =
    Future.successful(Forbidden(Json.toJson(InvalidEmpRef)))
  def genericAuthorisationError(message: String): Future[Result] =
    Future.successful(Unauthorized(Json.toJson(GenericAuthorizationError(message))))

  def errorHandler[A]: PartialFunction[EpayeResponse[A], Result] = {
    case EpayeJsonError(error) =>
      Logger.error(s"Upstream returned invalid json: $error")
      InternalServerError(Json.toJson(ApiErrorJson.InternalServerError))
    case EpayeNotFound =>
      NotFound(Json.toJson(EmpRefNotFound))
    case EpayeError(status, body) =>
      Logger.error(s"Upstream returned an error: status=$status")
      Logger.debug(s"Upstream error: body=$body")
      InternalServerError(Json.toJson(ApiErrorJson.InternalServerError))
    case EpayeException(message) =>
      Logger.error(s"Upstream threw an exception: $message")
      InternalServerError(Json.toJson(ApiErrorJson.InternalServerError))
  }
}

object ApiController {
  implicit val empRefPathBinder = new SimpleObjectBinder[EmpRef](EmpRef.fromIdentifiers, _.encodedValue)
}
