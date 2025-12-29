/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
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

package BlockDynasty.Economy.domain.result;

public enum ErrorCode {
    ACCOUNT_NOT_FOUND,
    ACCOUNT_DUPLICATED,
    ACCOUNT_BLOCKED,
    ACCOUNT_CAN_NOT_RECEIVE,
    ACCOUNT_ALREADY_EXISTS,
    ACCOUNT_NOT_HAVE_BALANCE,
    INSUFFICIENT_FUNDS,

    INVALID_AMOUNT,  //validacion de montos
    DECIMAL_NOT_SUPPORTED, //validacion de montos

    CURRENCY_NOT_FOUND,
    CURRENCY_ALREADY_EXISTS,
    CURRENCY_NOT_PAYABLE,
    CURRENCY_ARE_NOT_PAYABLE,
    CURRENCY_MUST_BE_DIFFERENT,
    CURRENCY_HAS_NO_EXCHANGE_RATE,
    CURRENCIES_NOT_INTERCHANGEABLE,

    OFFER_NOT_FOUND,
    OFFER_ALREADY_EXISTS,

    BANK_NOT_FOUND,
    BANK_ALREADY_EXISTS,
    BANK_NOT_HAVE_BALANCE,
    BANK_NOT_HAVE_CURRENCY,
    BANK_NAME_CANNOT_BE_EMPTY,

    UNKNOWN_ERROR,
    DATA_BASE_ERROR,
    REPOSITORY_NOT_SUPPORT_TOP,

    INVALID_ARGUMENT,
    NOT_IMPLEMENTED
    }
