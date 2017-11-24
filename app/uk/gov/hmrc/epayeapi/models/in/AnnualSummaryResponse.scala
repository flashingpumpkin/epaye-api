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

import org.joda.time.LocalDate

case class DebitAndCredit(
  debit: BigDecimal = 0,
  credit: BigDecimal = 0
)

case class Cleared(
  payment: BigDecimal = 0,
  credit: BigDecimal = 0
)


case class TaxMonth(month: Int) {
  def firstDay(year: TaxYear): LocalDate =
    year.firstDay.plusMonths(month - 1)

  def lastDay(year: TaxYear): LocalDate =
    year.firstDay.plusMonths(month).minusDays(1)
}

case class LineItem(
  taxYear: TaxYear,
  taxMonth: Option[TaxMonth],
  charges: DebitAndCredit,
  cleared: Cleared,
  balance: DebitAndCredit,
  dueDate: LocalDate,
  isSpecified: Boolean = false,
  codeText: Option[String] = None
)

case class AnnualTotal(
  charges: DebitAndCredit,
  cleared: Cleared,
  balance: DebitAndCredit
)

case class AnnualSummary(lineItems: Seq[LineItem], totals: AnnualTotal)

case class AnnualSummaryResponse(rti: AnnualSummary, nonRti: AnnualSummary)