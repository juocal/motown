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
package io.motown.ocpp.websocketjson.response.centralsystem;

import java.util.Date;

public class BootNotificationResponse implements CentralSystemResponse {

    private RegistrationStatus status;

    private Date currentTime;

    private int heartbeatInterval;

    public BootNotificationResponse(RegistrationStatus status, Date currentTime, int heartbeatInterval) {
        this.status = status;
        if(currentTime != null) {
            this.currentTime = new Date(currentTime.getTime());
        }
        this.heartbeatInterval = heartbeatInterval;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public Date getCurrentTime() {
        return currentTime != null ? new Date(currentTime.getTime()) : null;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }
}
