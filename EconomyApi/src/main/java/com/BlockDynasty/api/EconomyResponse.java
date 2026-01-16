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

package com.BlockDynasty.api;

/**
 * Indicates a typical Return for an Economy method.
 * It includes a {@link ResponseType} indicating whether the plugin currently being used for Economy actually allows
 * the method, or if the operation was a success or failure.
 *
 */
public class EconomyResponse {
    /**
     * Success or failure of call. Using Enum of ResponseType to determine valid
     * outcomes
     */
    private final ResponseType type;
    /**
     * Error message if the variable 'type' is ResponseType.FAILURE
     */
    private final String errorMessage;

    /**
     * Constructor for EconomyResponse
     * @param type Success or failure type of the operation
     * @param errorMessage Error message if necessary (commonly null)
     */
    private EconomyResponse(ResponseType type, String errorMessage) {
        this.type = type;
        this.errorMessage = errorMessage;
    }

    /**
     * Static method to quickly create a successful EconomyResponse
     * @return EconomyResponse of type SUCCESS
     */
    public static EconomyResponse success() {
        return new EconomyResponse(ResponseType.SUCCESS, null);
    }

    /**
     * Static method to quickly create a failed EconomyResponse
     * @param errorMessage Error message for the failure
     * @return EconomyResponse of type FAILURE
     */
    public static EconomyResponse failure(String errorMessage) {
        return new EconomyResponse(ResponseType.FAILURE, errorMessage);
    }

    /**
     * Static method to quickly create a not implemented EconomyResponse
     * @return EconomyResponse of type NOT_IMPLEMENTED
     */
    public static EconomyResponse notImplemented() {
        return new EconomyResponse(ResponseType.NOT_IMPLEMENTED, "Method not implemented");
    }

    /**
     * Checks if an operation was successful
     * @return Value
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }

    /**
     * Gets the type of the response
     * @return ResponseType of the operation, SUCCESS, FAILURE, or NOT_IMPLEMENTED
     */
    public ResponseType getType() {
        return type;
    }

    /**
     * Gets the error message of the response
     * @return Error message if applicable, OR null if not
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Enum for types of Responses indicating the status of a method call.
     */
    public static enum ResponseType {
        SUCCESS,
        FAILURE,
        NOT_IMPLEMENTED;
    }
}