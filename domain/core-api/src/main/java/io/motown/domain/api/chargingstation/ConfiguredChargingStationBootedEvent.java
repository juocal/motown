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

import io.motown.domain.api.security.IdentityContext;

import java.util.Map;

/**
 * {@code ConfiguredChargingStationBootedEvent} is the event which is published when a configured charging station has
 * booted.
 */
public final class ConfiguredChargingStationBootedEvent extends ChargingStationBootedEvent {
    /**
     * Creates a {@code ConfiguredChargingStationBootedEvent} with an identifier and a {@link java.util.Map} of
     * attributes.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          protocol identifier.
     * @param attributes        a {@link java.util.Map} of attributes. These attributes are additional information
     *                          provided by the charging station when it booted but which are not required by Motown.
     *                          Because {@link java.util.Map} implementations are potentially mutable a defensive copy
     *                          is made.
     * @param identityContext the identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code protocol} or {@code attributes} is {@code null}.
     */
    public ConfiguredChargingStationBootedEvent(ChargingStationId chargingStationId, String protocol, Map<String, String> attributes, IdentityContext identityContext) {
        super(chargingStationId, protocol, attributes, identityContext);
    }
}
