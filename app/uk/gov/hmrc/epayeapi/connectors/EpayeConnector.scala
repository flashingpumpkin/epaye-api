/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.epayeapi.connectors

import javax.inject.{Inject, Singleton}

import uk.gov.hmrc.domain.EmpRef
import uk.gov.hmrc.epayeapi.models.Formats._
import uk.gov.hmrc.epayeapi.models.in.{EpayeAnnualStatement, ApiResponse, EpayeTotalsResponse, TaxYear}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.http.ws.WSHttp

import scala.concurrent.{ExecutionContext, Future}

case class EpayeApiConfig(baseUrl: String)

@Singleton
case class EpayeConnector @Inject() (
  config: EpayeApiConfig,
  http: WSHttp,
  implicit val ec: ExecutionContext
) extends ConnectorBase {

  def getTotal(empRef: EmpRef, headers: HeaderCarrier): Future[ApiResponse[EpayeTotalsResponse]] = {
    val url =
      s"${config.baseUrl}" +
        s"/epaye" +
        s"/${empRef.encodedValue}" +
        s"/api/v1/annual-statement"

    get[EpayeTotalsResponse](url, headers)
  }

  def getAnnualStatement(empRef: EmpRef, taxYear: Option[TaxYear], headers: HeaderCarrier): Future[ApiResponse[EpayeAnnualStatement]] = {
    val url =
      s"${config.baseUrl}" +
        s"/epaye" +
        s"/${empRef.encodedValue}" +
        s"/api/v1/annual-statement" +
        taxYear
        .map(TaxYear.asString)
        .map(q => s"/$q")
        .getOrElse("")

    get[EpayeAnnualStatement](url, headers)
  }
}

