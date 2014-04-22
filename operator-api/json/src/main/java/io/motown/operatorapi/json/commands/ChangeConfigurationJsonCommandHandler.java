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
package io.motown.operatorapi.json.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.commandauthorization.CommandAuthorizationService;
import io.motown.operatorapi.viewmodel.model.ChangeConfigurationApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;

/**
 * Handles the Change Configuration JSON Command.
 */
class ChangeConfigurationJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "ChangeConfiguration";

    private DomainCommandGateway commandGateway;

    private ChargingStationRepository repository;

    private Gson gson;

    private CommandAuthorizationService commandAuthorizationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(String chargingStationId, JsonObject commandObject, IdentityContext identityContext) {
        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            ChargingStationId chargingStationIdObject = new ChargingStationId(chargingStationId);

            if (chargingStation != null && chargingStation.isAccepted() &&
                    commandAuthorizationService.isAuthorized(chargingStationIdObject, identityContext.getUserIdentity(), ChangeConfigurationApiCommand.class)) {
                ChangeConfigurationApiCommand command = gson.fromJson(commandObject, ChangeConfigurationApiCommand.class);

                commandGateway.send(new RequestChangeConfigurationItemCommand(chargingStationIdObject, new ConfigurationItem(command.getKey(), command.getValue()), identityContext), new CorrelationToken());
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Change configuration command not able to parse the payload, is your json correctly formatted?", ex);
        }
    }

    /**
     * Sets the command gateway.
     *
     * @param commandGateway the command gateway.
     */
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    /**
     * Sets the charging station repository.
     *
     * @param repository the charging station repository.
     */
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    /**
     * Sets the GSON instance.
     *
     * @param gson the GSON instance.
     */
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    /**
     * Sets the command authorization service to use. The command authorization service checks if a certain user is
     * allowed to execute a certain command.
     *
     * @param commandAuthorizationService    command authorization.
     */
    public void setCommandAuthorizationService(CommandAuthorizationService commandAuthorizationService) {
        this.commandAuthorizationService = commandAuthorizationService;
    }
}
