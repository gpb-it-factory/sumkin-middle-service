package com.gpb.sumkin_middle_service.service;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.gpb.sumkin_middle_service.dto.AccountDto;
import com.gpb.sumkin_middle_service.dto.RegAccountDto;
import com.gpb.sumkin_middle_service.entities.AccountGpb;
import com.gpb.sumkin_middle_service.entities.MyError;
import com.gpb.sumkin_middle_service.entities.UserGpb;
import com.gpb.sumkin_middle_service.repositories.AccountGpbRepository;
import com.gpb.sumkin_middle_service.repositories.UserGpbRepository;
import org.instancio.Instancio;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
class AccountServiceTest {

    private static List<AccountGpb> accounts;
    private static UserGpb userGpb;
    private static int AMOUNT_OF_TEST_ENTITIES = 10;
    private AccountGpb oneMoreAccount = Instancio.create(AccountGpb.class);

    @Autowired
    private AccountGpbRepository accountGpbRepository;

    @Autowired
    private UserGpbRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0")
            .withInitScript("init.sql")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5443), new ExposedPort(5432)))
            ));


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    public static void setup() {
        accounts = Instancio.ofList
                        (AccountGpb.class)
                .size(AMOUNT_OF_TEST_ENTITIES)
                .create();
        userGpb = Instancio.create(UserGpb.class);

    }

    @AfterAll
    public static void teardownDatabase() {
        postgres.stop();
    }

    @Test
    void shouldFailToRegisterAccountWithResponse404IfUserNotRegistered() {
        RegAccountDto regAccountDto = new RegAccountDto(oneMoreAccount.getAccountName());
        Long tgId = oneMoreAccount.getTgId();
        ResponseEntity response = accountService.registerAccount(tgId,
                regAccountDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        try {
            MyError myError = (MyError) response.getBody();
            assertNotNull(myError);
            assertThat(myError.getMessage()).isEqualTo(
                    "Пользователь c tgId " + tgId + " не зарегистрирован"
            );
        } catch (ClassCastException e) {
            System.out.println("Something went wrong - unsuccessful cast of response" + e.getMessage());
        }
    }

    @Test
    void shouldRegisterAccountWithResponse201() {
        userRepository.save(userGpb);
        Long tgId = userGpb.getTgId();

        oneMoreAccount = Instancio.of(AccountGpb.class)
                .set(field(AccountGpb::getTgId), userGpb.getTgId())
                .create();
        RegAccountDto regAccountDto = new RegAccountDto(oneMoreAccount.getAccountName());
        ResponseEntity response = accountService.registerAccount(tgId,
                regAccountDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        try {
            AccountDto newAccount = (AccountDto) response.getBody();
            assertNotNull(newAccount);
            assertThat(newAccount.getAccountName()).isEqualTo(oneMoreAccount.getAccountName());
            assertThat(newAccount.getAmount()).isEqualTo(BigDecimal.valueOf(5000));
            assertThat(newAccount.getId()).isNotNull();
        } catch (ClassCastException e) {
            System.out.println("Something went wrong - unsuccessful cast of response" + e.getMessage());
        }
    }

    @Test
    void shouldGetAllAccounts() {
        accountGpbRepository.saveAll(accounts);

        List<AccountGpb> savedAccounts = accountGpbRepository.findAll();
        List<AccountGpb> allAccounts = accountService.getAllAccounts(0, 50);
        assertThat(savedAccounts.size()).isEqualTo(AMOUNT_OF_TEST_ENTITIES);
        assertThat(allAccounts).isEqualTo(savedAccounts);
    }

    @Test
    void shouldGetAccountByUserName() {

    }
}