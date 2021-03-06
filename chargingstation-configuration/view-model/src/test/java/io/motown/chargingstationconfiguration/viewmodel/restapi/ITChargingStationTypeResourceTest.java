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
package io.motown.chargingstationconfiguration.viewmodel.restapi;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.grizzly2.web.GrizzlyWebTestContainerFactory;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ChargingStationTypeRepository;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ManufacturerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:jersey-test-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ITChargingStationTypeResourceTest extends JerseyTest {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NOT_FOUND = 404;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final String BASE_URI = "http://localhost:9998/config/api/chargingstationtypes";

    private Client client;

    @Autowired
    private ChargingStationTypeRepository chargingStationTypeRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public ITChargingStationTypeResourceTest() throws TestContainerException {
        super(new GrizzlyWebTestContainerFactory());
    }

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder()
                .contextParam("contextConfigLocation", "classpath:jersey-test-config.xml")
                .contextListenerClass(ContextLoaderListener.class)
                .contextPath("/config")
                .requestListenerClass(RequestContextListener.class)
                .servletClass(SpringServlet.class)
                .servletPath("/api")
                .initParam("com.sun.jersey.config.property.packages", "io.motown.chargingstationconfiguration.viewmodel.restapi;io.motown.utils.rest.jax_rs")
                .build();
    }

    @Override
    public Client client() {
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        return Client.create(config);
    }

    @Before
    public void setUp() {
        this.client = client();
    }

    @Test
    public void testCreateChargingStationType() {
        ChargingStationType cst = getChargingStationType();
        ClientResponse response = client.resource(BASE_URI)
                .type(ApiVersion.V1_JSON)
                .accept(ApiVersion.V1_JSON)
                .post(ClientResponse.class, cst);

        assertEquals(CREATED, response.getStatus());
    }

    @Test
    public void testCreateChargingStationTypeUniqueConstraintViolation() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCode("MAN01");
        manufacturer = manufacturerRepository.createOrUpdate(manufacturer);

        ChargingStationType c1 = getChargingStationType();
        c1.setManufacturer(manufacturer);
        chargingStationTypeRepository.createOrUpdate(c1);

        ChargingStationType c2 = getChargingStationType();
        c2.setManufacturer(manufacturer);
        ClientResponse response = client.resource(BASE_URI)
                .type(ApiVersion.V1_JSON)
                .accept(ApiVersion.V1_JSON)
                .post(ClientResponse.class, c2);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void testUpdateChargingStationType() {
        ChargingStationType cst = getChargingStationType();
        cst = chargingStationTypeRepository.createOrUpdate(cst);

        cst.setCode("TEST02");
        ClientResponse response = client.resource(BASE_URI)
                .path("/" + cst.getId())
                .type(ApiVersion.V1_JSON)
                .accept(ApiVersion.V1_JSON)
                .put(ClientResponse.class, cst);

        assertEquals(OK, response.getStatus());
        assertEquals(cst.getCode(), response.getEntity(ChargingStationType.class).getCode());
    }

    @Test
    public void testGetChargingStationTypes() {
        ClientResponse response = client.resource(BASE_URI)
                .accept(ApiVersion.V1_JSON)
                .get(ClientResponse.class);

        assertEquals(OK, response.getStatus());
    }

    @Test
    public void testGetChargingStationType() {
        ChargingStationType cst = getChargingStationType();
        cst = chargingStationTypeRepository.createOrUpdate(cst);

        ClientResponse response = client.resource(BASE_URI)
                .path("/" + cst.getId())
                .accept(ApiVersion.V1_JSON)
                .get(ClientResponse.class);

        assertEquals(OK, response.getStatus());
    }

    @Test
    public void testGetChargingStationTypeNotFound() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/1")
                .accept(ApiVersion.V1_JSON)
                .get(ClientResponse.class);

        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testDeleteChargingStationType() {
        ChargingStationType cst = getChargingStationType();
        cst = chargingStationTypeRepository.createOrUpdate(cst);

        ClientResponse response = client.resource(BASE_URI)
                .path("/" + cst.getId())
                .accept(ApiVersion.V1_JSON)
                .delete(ClientResponse.class);

        assertEquals(OK, response.getStatus());
    }

    @Test
    public void testDeleteChargingStationTypeNotFound() {
        ClientResponse response = client.resource(BASE_URI)
                .path("/1")
                .accept(ApiVersion.V1_JSON)
                .delete(ClientResponse.class);

        assertEquals(NOT_FOUND, response.getStatus());
    }

    private ChargingStationType getChargingStationType() {
        ChargingStationType chargingStationType = new ChargingStationType();
        chargingStationType.setCode("TEST01");
        return chargingStationType;
    }
}
