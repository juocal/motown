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
package io.motown.ocpp.websocketjson.request.handler;

import io.motown.domain.api.chargingstation.Location;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class LocationTranslatorTest {

    @Test(expected = AssertionError.class)
    public void testTranslate() throws Exception {
        new LocationTranslator("Not.A.Location").translate();
    }

    @RunWith(Parameterized.class)
    public static class TranslateTest {

        private final String input;
        private final Location expected;

        public TranslateTest(String input, Location expected) {
            this.input = input;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static List<Object[]> getParameters() {
            return Arrays.asList(new Object[][]{
                    {"Body", Location.BODY},
                    {"Inlet", Location.INLET},
                    {"Outlet", Location.OUTLET},
                    {null, Location.OUTLET},
                    {"", Location.OUTLET}
            });
        }

        @Test
        public void testTranslate() {
            LocationTranslator translator = new LocationTranslator(input);

            Location actual = translator.translate();

            assertEquals(actual, expected);
        }
    }
}
