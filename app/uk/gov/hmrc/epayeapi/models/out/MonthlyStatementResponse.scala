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
import uk.gov.hmrc.domain.EmpRef
import uk.gov.hmrc.epayeapi.models.{TaxMonth, TaxYear}
import uk.gov.hmrc.epayeapi.models.in.MonthlyStatementJson

case class MonthlyStatementResponse(
  taxOfficeNumber: String,
  taxOfficeReference: String,
  taxYear: TaxYear,
  taxMonth: TaxMonth,
  rtiCharges: Seq[Charge],
  interest: Seq[Charge],
  allocatedCredits: Seq[Charge],
  allocatedPayments: Seq[Payment],
  dueDate: LocalDate,
  summary: MonthlySummary,
  links: MonthlyStatementLinks
)

object MonthlyStatementResponse {
  def apply(empRef: EmpRef, taxYear: TaxYear, taxMonth: TaxMonth, json: MonthlyStatementJson): MonthlyStatementResponse =
    MonthlyStatementResponse(
      taxOfficeNumber = empRef.taxOfficeNumber,
      taxOfficeReference = empRef.taxOfficeReference,
      taxYear = taxYear,
      taxMonth = taxMonth,
      rtiCharges =
      json.charges.fps.items.map { c => Charge(c.code.name, c.amount) } ++
        json.charges.cis.items.map { c => Charge(c.code.name, c.amount) } ++
        json.charges.eps.items.map { c => Charge(c.code.name, c.amount) },
      interest =
        Seq(Charge("INTEREST", json.charges.others)),
      allocatedCredits =
        json.credits.fps.items.map { c => Charge(c.code.name, c.amount) } ++
          json.credits.cis.items.map { c => Charge(c.code.name, c.amount) } ++
          json.credits.eps.items.map { c => Charge(c.code.name, c.amount) },
      allocatedPayments =
        json.payments.items.map { p => Payment(p.dateOfPayment, p.amount) },
      dueDate = json.balance.dueDate,
      summary =
        MonthlySummary(
          json.charges.total - json.charges.others,
          json.charges.others,
          json.credits.total,
          json.payments.total,
          json.balance.total
        ),
      links = MonthlyStatementLinks(empRef, taxYear,taxMonth)
    )
}

case class Charge(
  code: String,
  amount: BigDecimal
)
case class Payment(
  paymentDate: LocalDate,
  amount: BigDecimal
)
case class MonthlySummary(
  amount: BigDecimal,
  interest: BigDecimal,
  clearedByCredits: BigDecimal,
  clearedByPayments: BigDecimal,
  balance: BigDecimal
)
case class MonthlyStatementLinks(
  empRefs: Link,
  statements: Link,
  annualStatement: Link,
  self: Link,
  next: Link,
  previous: Link
)

object MonthlyStatementLinks {
  def apply(empRef: EmpRef, taxYear: TaxYear, taxMonth: TaxMonth): MonthlyStatementLinks =
    MonthlyStatementLinks(
      empRefs = Link.empRefsLink(),
      statements = Link.summaryLink(empRef),
      annualStatement = Link.anualStatementLink(empRef, taxYear),
      self = Link.monthlyStatementLink(empRef, taxYear, taxMonth),
      next =
        if (taxMonth.month == 12) {
          Link.monthlyStatementLink(empRef, TaxYear(taxYear.yearFrom + 1), TaxMonth(1))
        } else {
          Link.monthlyStatementLink(empRef, taxYear, TaxMonth(taxMonth.month - 1))
        },
      previous =
        if (taxMonth.month == 1) {
          Link.monthlyStatementLink(empRef, TaxYear(taxYear.yearFrom - 1), TaxMonth(12))
        } else {
          Link.monthlyStatementLink(empRef, taxYear, TaxMonth(taxMonth.month + 1))
        }
    )
}
