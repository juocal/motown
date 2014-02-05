/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.domain.api.chargingstation;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code UpdateDiagnosticsUploadStatusCommand} is the command which is published upon receipt of the status
 * notification the charging station sends when the upload has finished or not.
 */
public final class UpdateDiagnosticsUploadStatusCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final boolean isUploaded;

    /**
     * Creates a {@code UpdateDiagnosticsUploadStatusCommand} with an identifier and an indicator if the file has
     * been successfully uploaded or not.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param isUploaded        the status of the upload, in case false the upload failed
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public UpdateDiagnosticsUploadStatusCommand(ChargingStationId chargingStationId, boolean isUploaded) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.isUploaded = isUploaded;
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return boolean indicating if the upload has succeeded or not
     */
    public boolean isUploaded() {
        return isUploaded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, isUploaded);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UpdateDiagnosticsUploadStatusCommand other = (UpdateDiagnosticsUploadStatusCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.isUploaded, other.isUploaded);
    }
}
