package io.gaurs.sbm;

import io.gaurs.sbm.repositories.EmployeeRepository;
import io.gaurs.sbm.services.EmployeePersistenceService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import spock.mock.DetachedMockFactory;
import spock.mock.MockFactory;

@TestConfiguration
@ComponentScan("io.gaurs.sbm.endpoints")
public class MockBeanConfig{
    private final MockFactory mockFactory = new DetachedMockFactory();

    @Bean
    public EmployeePersistenceService mockEmployeePersistenceService(){
        return mockFactory.Mock(EmployeePersistenceService.class);
    }

    @Bean
    public EmployeeRepository mockEmployeeRepository(){
        return mockFactory.Mock(EmployeeRepository.class);
    }
}
