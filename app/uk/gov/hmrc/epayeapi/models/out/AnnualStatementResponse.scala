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

package uk.gov.hmrc.epayeapi.models.out

import org.joda.time.LocalDate
import uk.gov.hmrc.epayeapi.models.in.EpayeAnnualStatement

case class PeriodJson(firstDay: LocalDate, lastDay: LocalDate)

case class NonRtiChargesJson(
  code: String,
  taxPeriod: PeriodJson,
  amount: BigDecimal,
  clearedByCredits: BigDecimal,
  clearedByPayments: BigDecimal,
  balance: BigDecimal,
  dueDate: LocalDate
)

case class SummaryJson(
  rtiCharges: ChargesSummaryJson,
  nonRtiCharges: ChargesSummaryJson,
  unallocated: PaymentsAndCreditsJson
)
case class ChargesSummaryJson(
  amount: BigDecimal,
  clearedByCredits: BigDecimal,
  clearedByPayments: BigDecimal,
  balance: BigDecimal
)
case class PaymentsAndCreditsJson(
  payments: BigDecimal,
  credits: BigDecimal
)

case class EmbeddedJson(rtiCharges: RtiChargesJson)
case class RtiChargesJson(
  taxMonth: TaxMonthJson,
  amount: BigDecimal,
  clearedByCredits: BigDecimal,
  clearedByPayments: BigDecimal,
  balance: BigDecimal,
  dueDate: LocalDate,
  _links: SelfLinks
)
case class TaxMonthJson(
  number: Int,
  firstDay: LocalDate,
  lastDay: LocalDate
)
case class SelfLinks(
  self: Link
)

case class AnnualStatementLinks(
  empRefs: Link,
  statements: Link,
  self: Link,
  next: Link,
  previous: Link
)

case class AnnualStatementResponse(
  taxYear: PeriodJson,
  nonRtiCharges: NonRtiChargesJson,
  summary: SummaryJson,
  _embedded: EmbeddedJson,
  _links: AnnualStatementLinks
)

object AnnualStatementResponse {
  def apply(annualStatement: EpayeAnnualStatement): AnnualStatementResponse = ???
}