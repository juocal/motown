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
package io.motown.vas.viewmodel;

import io.motown.domain.api.chargingstation.Connector;
import io.motown.domain.api.chargingstation.Evse;
import io.motown.vas.viewmodel.model.*;

import java.util.HashSet;
import java.util.Set;

public class ConfigurationConversionService {

    /**
     * Creates a set of {@code ConnectorType} based on the EVSEs. Duplicates will be filtered.
     *
     * @param evses list of EVSEs
     * @return set of connector types
     */
    public Set<ConnectorType> getConnectorTypesFromEvses(Set<Evse> evses) {
        Set<ConnectorType> connectorTypes = new HashSet<>();
        for (Evse evse : evses) {
            for (Connector connector : evse.getConnectors()) {
                connectorTypes.add(ConnectorType.fromConnectorType(connector.getConnectorType()));
            }
        }
        return connectorTypes;
    }

    /**
     * Determines the charge mode based on the first connector, because VAS does not support multiple protocols for a
     * single charging station. If no charge mode can be determined UNSPECIFIED will be returned.
     *
     * @param evses list of EVSEs.
     * @return charge mode or UNSPECIFIED if no specific charge mode can be determined.
     */
    public ChargeMode getChargeModeFromEvses(Set<Evse> evses) {
        ChargeMode chargeMode = ChargeMode.UNSPECIFIED;
        for (Evse evse : evses) {
            if (!evse.getConnectors().isEmpty()) {
                chargeMode = ChargeMode.fromChargingProtocol(evse.getConnectors().get(0).getChargingProtocol());
                break;
            }
        }
        return chargeMode;
    }

    /**
     * Returns a set of {@code io.motown.vas.viewmodel.persistence.entities.Evse}s based on
     * {@code io.motown.domain.api.chargingstation.Evse}s. The state of all EVSEs will be UNKNOWN. State will be updated
     * in the ComponentStatusNotificationReceivedEvent handler.
     *
     * @param eventEvses list of EVSEs.
     * @return set of VAS EVSEs.
     */
    public Set<io.motown.vas.viewmodel.persistence.entities.Evse> getEvsesFromEventEvses(Set<Evse> eventEvses) {
        Set<io.motown.vas.viewmodel.persistence.entities.Evse> evses = new HashSet<>();
        for (Evse evse : eventEvses) {
            evses.add(new io.motown.vas.viewmodel.persistence.entities.Evse(evse.getEvseId().getNumberedId(), ComponentStatus.UNKNOWN));
        }
        return evses;
    }

    /**
     * Returns a set of {@code io.motown.vas.viewmodel.model.ChargingCapability}s based on the EVSEs.
     *
     * @param evses list of EVSEs.
     * @return set of ChargingCapability.
     */
    public Set<ChargingCapability> getChargingCapabilitiesFromEvses(Set<Evse> evses) {
        Set<ChargingCapability> chargingCapabilities = new HashSet<>();

        for (Evse evse : evses) {
            for (Connector connector : evse.getConnectors()) {
                chargingCapabilities.add(ChargingCapability.fromConnector(connector));
            }
        }

        return chargingCapabilities;
    }

}
