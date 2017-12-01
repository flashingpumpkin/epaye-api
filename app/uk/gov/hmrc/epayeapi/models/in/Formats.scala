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

package uk.gov.hmrc.epayeapi.models.in

import play.api.libs.json.Json.format
import play.api.libs.json._
import uk.gov.hmrc.epayeapi.models.{CommonFormats, TaxMonth, TaxYear}

trait Formats extends CommonFormats {
  implicit lazy val debitAndCreditFormat: Format[DebitAndCredit] = format[DebitAndCredit]
  implicit lazy val clearedFormat: Format[Cleared] = format[Cleared]
  implicit lazy val annualTotalFormat: Format[AnnualTotal] = format[AnnualTotal]
  implicit lazy val lineItemFormat: Format[LineItem] = format[LineItem]
  implicit lazy val annualSummaryFormat: Format[AnnualSummary] = format[AnnualSummary]
  implicit lazy val annualSummaryResponseFormat: Format[AnnualSummaryResponse] = format[AnnualSummaryResponse]

  implicit lazy val epayeTotals: Format[EpayeTotals] = format[EpayeTotals]
  implicit lazy val epayeTotalsItems: Format[EpayeTotalsItem] = format[EpayeTotalsItem]
  implicit lazy val epayeTotalsResponse: Format[EpayeTotalsResponse] = format[EpayeTotalsResponse]

  implicit lazy val monthlyStatementJsonFormat: Format[MonthlyStatementJson] = format[MonthlyStatementJson]

  implicit lazy val monthlyChargesJsonFormat: Format[MonthlyChargesJson] = format[MonthlyChargesJson]
  implicit lazy val monthlyCreditsJsonFormat: Format[MonthlyCreditsJson] = format[MonthlyCreditsJson]
  implicit lazy val monthlyChargesDetailsJsonFormat: Format[MonthlyChargesDetailsJson] = format[MonthlyChargesDetailsJson]
  implicit lazy val monthlyStatementItemJsonFormat: Format[MonthlyStatementItemJson] = format[MonthlyStatementItemJson]
  implicit lazy val codeFormat: Format[Code] = format[Code]
  implicit lazy val monthlyPaymentDetailsJsonFormat: Format[MonthlyPaymentDetailsJson] = format[MonthlyPaymentDetailsJson]
  implicit lazy val monthlyPaymentItemJsonFormat: Format[MonthlyPaymentItemJson] = format[MonthlyPaymentItemJson]
  implicit lazy val monthlyBalanceJsonFormat: Format[MonthlyBalanceJson] = format[MonthlyBalanceJson]
}

object Formats extends Formats
