/*
 * Copyright (c) 2015-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
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
package org.opencypher.tools.tck

import cucumber.api.DataTable
import org.opencypher.tools.tck.constants.TCKSideEffects

import scala.collection.JavaConverters._

/**
  * Validates side effects expectations. A valid side effect has one of the specified names in TCKSideEffects, and a
  * quantity that is an integer greater than zero.
  */
object validateSideEffects extends (DataTable => Option[String]) {

  override def apply(table: DataTable): Option[String] = {
    val keys = table.transpose().topCells().asScala
    val values = table.transpose().cells(1).asScala.head.asScala

    val msg = s"""${checkKeys(keys)}
                 |${checkValues(values)}""".stripMargin

    if (msg.trim.isEmpty) None
    else Some(msg)
  }

  def checkKeys(keys: Seq[String]): String = {
    val badKeys = keys.filterNot(TCKSideEffects.ALL)

    if (badKeys.isEmpty) ""
    else s"Invalid side effect keys: ${badKeys.mkString(", ")}"
  }

  def checkValues(values: Seq[String]): String = {
    val badValues = values.filterNot { value =>
      value.forall(Character.isDigit) && Integer.valueOf(value) > 0
    }
    if (badValues.isEmpty) ""
    else s"Invalid side effect values: ${badValues.mkString(", ")}"
  }

}
