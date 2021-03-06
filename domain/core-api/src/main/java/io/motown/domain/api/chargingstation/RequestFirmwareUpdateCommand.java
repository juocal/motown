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

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.security.IdentityContext;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestFirmwareUpdateCommand} is the command which is published to notify the charging station to update
 * it's firmware.
 */
public final class RequestFirmwareUpdateCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String updateLocation;

    private final Date retrieveDate;

    private final Map<String, String> attributes;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code RequestFirmwareUpdateCommand}.
     *
     * @param chargingStationId the charging station identifier.
     * @param updateLocation    the location to download the firmware update from.
     * @param retrieveDate      the moment the charging station should start retrieving the firmware update from the update location.
     * @param attributes        optional attributes like retry settings.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code updateLocation}, {@code retrieveDate},
     *                              {@code attributes} or {@code identityContext} is {@code null}.
     * @throws IllegalArgumentException if {@code updateLocation} is empty.
     */
    public RequestFirmwareUpdateCommand(ChargingStationId chargingStationId, String updateLocation, Date retrieveDate, Map<String, String> attributes, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);

        checkNotNull(updateLocation);
        checkArgument(!updateLocation.isEmpty());
        this.updateLocation = updateLocation;
        this.retrieveDate = new Date(checkNotNull(retrieveDate).getTime());
        this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * @return the location to download the firmware update from
     */
    public String getUpdateLocation() {
        return updateLocation;
    }

    /**
     * @return the moment the charging station should start retrieving the firmware update from the updatelocation
     */
    public Date getRetrieveDate() {
        return new Date(retrieveDate.getTime());
    }

    /**
     * @return the optional attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, updateLocation, retrieveDate, attributes, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RequestFirmwareUpdateCommand other = (RequestFirmwareUpdateCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.updateLocation, other.updateLocation) && Objects.equals(this.retrieveDate, other.retrieveDate) && Objects.equals(this.attributes, other.attributes) && Objects.equals(this.identityContext, other.identityContext);
    }
}
