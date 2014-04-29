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
import io.motown.domain.api.security.UserIdentity;
import io.motown.domain.commandauthorization.CommandAuthorizationService;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.model.RequestResetChargingStationApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;

class RequestResetChargingStationJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "ResetChargingStation";

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
    public void handle(String chargingStationId, JsonObject commandObject, IdentityContext identityContext) throws UserIdentityUnauthorizedException {
        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.communicationAllowed()) {
                ChargingStationId csId = new ChargingStationId(chargingStationId);
                UserIdentity userIdentity = identityContext.getUserIdentity();

                RequestResetChargingStationApiCommand command = gson.fromJson(commandObject, RequestResetChargingStationApiCommand.class);

                if("hard".equalsIgnoreCase(command.getType())) {
                    checkAuthorization(csId, userIdentity, RequestHardResetChargingStationCommand.class);
                    commandGateway.send(new RequestHardResetChargingStationCommand(csId, identityContext), new CorrelationToken());
                } else {
                    checkAuthorization(csId, userIdentity, RequestSoftResetChargingStationCommand.class);
                    commandGateway.send(new RequestSoftResetChargingStationCommand(csId, identityContext), new CorrelationToken());
                }
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Configure command not able to parse the payload, is your json correctly formatted?", ex);
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

    private void checkAuthorization(ChargingStationId chargingStationId, UserIdentity userIdentity, Class commandClass) throws UserIdentityUnauthorizedException {
        if (!commandAuthorizationService.isAuthorized(chargingStationId, userIdentity, commandClass)) {
            throw new UserIdentityUnauthorizedException(chargingStationId.getId(), userIdentity, commandClass);
        }
    }
}
