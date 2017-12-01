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

package uk.gov.hmrc.epayeapi.models

import org.joda.time.LocalDate

import scala.util.{Success, Try}

case class TaxMonth(month: Int) {
  def firstDay(year: TaxYear): LocalDate =
    year.firstDay.plusMonths(month - 1)

  def lastDay(year: TaxYear): LocalDate =
    year.firstDay.plusMonths(month).minusDays(1)

  def asString: String =
    month.toString
}

object TaxMonth {
  def fromUrlEncodedValue(value: String): TaxMonth =
    Try(value.toInt) match {
      case Success(validMonth) if validMonth >= 1 && validMonth <= 12 =>
        TaxMonth(validMonth)
      case _ =>
        throw new IllegalArgumentException("Tax month requires a single integer between 1 and 12")
    }

  def toUrlEncodedValue(taxMonth: TaxMonth): String = taxMonth.asString

  implicit def toTaxMonth(month: Int): TaxMonth = TaxMonth(month)
}

