package com.example.wallet_api.repository;


import com.example.wallet_api.entity.Wallet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootTest
class WalletJpaTest {

    @Autowired
    WalletJpa walletJpa;

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private TransactionTemplate transactionTemplate;

    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static List<Wallet> wallets;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void beforeEach() {

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        wallets = walletJpa.saveAll(List.of(
                Wallet.builder().balance(BigDecimal.TEN).build(),
                Wallet.builder().balance(BigDecimal.TEN).build(),
                Wallet.builder().balance(BigDecimal.TEN).build()));
    }

    @AfterEach
    void afterEach() {
        walletJpa.deleteAll();
    }

    @Test
    @DisplayName("Save wallets list")
    void testSaveList() {
        walletJpa.save(Wallet.builder().balance(BigDecimal.ZERO).build());
        assertEquals(4, walletJpa.findAll().size());
    }

    @Test
    @DisplayName("Find by uuid ")
    void testFindById() {
        UUID uuid = wallets.get(0).getWalletId();

        assertTrue(transactionTemplate.execute(item -> {
            Optional<Wallet> wallet = walletJpa.findById(uuid);
            return wallet.isPresent();
        }).booleanValue());

    }

    @Test
    @DisplayName("Find by un correct uuid")
    void testFindByIsMissingIdTest() {
        UUID uuid = UUID.randomUUID();

        assertFalse(transactionTemplate.execute(item -> {
            Optional<Wallet> wallet = walletJpa.findById(uuid);
            return wallet.isPresent();
        }).booleanValue());
    }

    @Test
    @DisplayName("Test PESSIMISTIC_WRITE lock works")
    void testPessimisticWriteLock() throws InterruptedException {

        UUID uuid = wallets.get(0).getWalletId();
        AtomicBoolean result = new AtomicBoolean(false);

        Thread t1 = new Thread(() -> {

            assertTrue(transactionTemplate.execute(status -> {
                walletJpa.findById(uuid);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    return false;
                }
                return true;
            }).booleanValue());
        });

        Thread t2 = new Thread(() -> {

            long start = System.currentTimeMillis();
            transactionTemplate.execute(status -> walletJpa.findById(uuid));
            long end = System.currentTimeMillis() - start;

            result.set(end >= 2000);
        });

        t1.start();
        Thread.sleep(100);
        t2.start();

        t1.join();
        t2.join();

        assertTrue(result.get());
    }

}

