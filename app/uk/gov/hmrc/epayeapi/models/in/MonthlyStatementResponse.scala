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

case class MonthlyStatementJson(
  charges: MonthlyChargesJson,
  credits: MonthlyCreditsJson,
  payments: MonthlyPaymentDetailsJson,
  hasSpecifiedCharges: Boolean,
  balance: MonthlyBalanceJson
)

case class MonthlyChargesJson(
  fps: MonthlyChargesDetailsJson,
  cis: MonthlyChargesDetailsJson,
  eps: MonthlyChargesDetailsJson,
  others: BigDecimal,
  total: BigDecimal
)

case class MonthlyCreditsJson(
  fps: MonthlyChargesDetailsJson,
  cis: MonthlyChargesDetailsJson,
  eps: MonthlyChargesDetailsJson,
  total: BigDecimal
)

case class MonthlyChargesDetailsJson(
  items: Seq[MonthlyStatementItemJson],
  total: BigDecimal
)

case class MonthlyStatementItemJson(
  code: Code,
  amount: BigDecimal
)

case class MonthlyPaymentDetailsJson(
  items: Seq[MonthlyPaymentItemJson],
  total: BigDecimal
)

case class MonthlyPaymentItemJson(
  dateOfPayment: LocalDate,
  amount: BigDecimal
)

case class MonthlyBalanceJson(
  total: BigDecimal,
  dueDate: LocalDate
)

