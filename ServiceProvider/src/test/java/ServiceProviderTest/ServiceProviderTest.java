/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ServiceProviderTest;

import net.blockdynasty.providers.services.ServiceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceProviderTest {
    private MockService mockService2 = new MockService() {
        @Override
        public String getId() {
            return "Service1";
        }
    };
    private  MockService mockService3 = new MockService() {
        @Override
        public String getId() {
            return "Service2";
        }
    };
    private  MockService mockService4 = new MockService() {
        @Override
        public String getId() {
            return "Service3";
        }
    };

    private MockServiceSecondary mockServiceSecondary = new MockServiceSecondary() {};

    @BeforeEach
    public void setup() {
        ServiceProvider.register(MockServiceSecondary.class, mockServiceSecondary);
        ServiceProvider.register(MockService.class, mockService2);
        ServiceProvider.register(MockService.class, mockService3);
        ServiceProvider.register(MockService.class, mockService4);
    }

    @AfterEach
    public void tearDown() {
        ServiceProvider.unregister(MockServiceSecondary.class, mockServiceSecondary);
        ServiceProvider.unregister(MockService.class, mockService2);
        ServiceProvider.unregister(MockService.class, mockService3);
        ServiceProvider.unregister(MockService.class, mockService4);
    }
    @Test
    public void testRegisteredServices() {
        assertNotNull(ServiceProvider.get(MockService.class, mockService -> mockService.getId().equals("Service1")));
        assertNotNull(ServiceProvider.get(MockService.class, mockService -> mockService.getId().equals("Service2")));
        assertNotNull(ServiceProvider.get(MockService.class, mockService -> mockService.getId().equals("Service3")));
    }

    @Test
    public void testGetFirstServiceRegistered() {
        MockService service = ServiceProvider.get(MockService.class);
        assertNotNull(service);
        assertEquals("Service1", service.getId());
    }

    @Test
    public void testGetServiceWithPredicate() {
        assertNotNull(ServiceProvider.get(MockService.class, mockService -> mockService.getId().equals("Service2")));
    }

    @Test
    public void testRemoveServiceWithReference() {
        ServiceProvider.unregister(MockService.class, mockService3);
        assertNull(ServiceProvider.get(MockService.class, mockService -> mockService.getId().equals("Service2")));
    }

    @Test
    public void testRemoveServiceWithoutReference() {
        MockService fakeService = new MockService() {
            @Override
            public String getId() {
                return "Service1";
            }
        };

        MockService proxyService = ServiceProvider.get(MockService.class, mockService -> mockService.getId().equals("Service1"));

        ServiceProvider.unregister(MockService.class, fakeService);
        ServiceProvider.unregister(MockService.class, proxyService);
        assertNotNull(ServiceProvider.get(MockService.class, mockService -> mockService.getId().equals("Service1")));
    }
}
