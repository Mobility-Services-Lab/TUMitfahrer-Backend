/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tumitfahrer.util.notification;

public class GcmResponse {

    boolean hasErrors = false;
    String errorMessage;
    boolean hasCanonicalId = false;
    boolean hasMessageError = false;
    String canonicalId = null;

    public boolean hasErrors() {
        return hasErrors;
    }

    public void setErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public boolean hasCanonicalId() {
        return hasCanonicalId;
    }

    public void setCanonicalId(boolean hasCanonicalId) {
        this.hasCanonicalId = hasCanonicalId;
    }

    public String getCanonicalId() {
        return canonicalId;
    }

    public void setCanonicalId(String canonicalId) {
        this.canonicalId = canonicalId;
    }

    public boolean hasMessageError() {
        return hasMessageError;
    }

    public void setMessageError(boolean hasMessageError) {
        this.hasMessageError = hasMessageError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
