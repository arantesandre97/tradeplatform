package org.mypersonalprojects.tradeplatform.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mypersonalprojects.tradeplatform.domain.Account;
import org.mypersonalprojects.tradeplatform.domain.AssetEnum;
import org.mypersonalprojects.tradeplatform.infra.dto.BalanceDTO;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountDatabaseRepository;

public class DepositUseCaseTest {
    private AccountDatabaseRepository accountRepository;
    private DepositUseCase depositUseCase;

    @BeforeEach
    void setUp() {
        this.accountRepository = mock(AccountDatabaseRepository.class);
        this.depositUseCase = new DepositUseCase(accountRepository);
    }

    @Test
    @DisplayName("Deve realizar deposito com sucesso")
    void shouldDepositAmount() throws Exception {
        var account = new Account (
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        var balanceDto = new BalanceDTO("BTC", 100.0);

        when(accountRepository.get(account.getId())).thenReturn(account);

        depositUseCase.execute(account.getId(), balanceDto);
        assertEquals(account.getBalance(AssetEnum.BTC).getAmount(), 100.0);
    }

    @Test
    @DisplayName("Não deve realizar deposito se a conta não existir")
    void shouldThrowExceptionWhenDepositAmount() {
        var accountId = UUID.randomUUID();
        var balanceDto = new BalanceDTO("BTC", 100.0);

        when(accountRepository.get(any(UUID.class))).thenThrow(new RuntimeException("Account not found"));

        var exception = assertThrows(RuntimeException.class, () -> 
            depositUseCase.execute(accountId, balanceDto)
        );

        assertEquals(exception.getMessage(), "Account not found");
    }
}
