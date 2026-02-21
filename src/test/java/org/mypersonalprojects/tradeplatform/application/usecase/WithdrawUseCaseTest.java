package org.mypersonalprojects.tradeplatform.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mypersonalprojects.tradeplatform.domain.Account;
import org.mypersonalprojects.tradeplatform.domain.AssetEnum;
import org.mypersonalprojects.tradeplatform.infra.dto.BalanceDTO;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountDatabaseRepository;

public class WithdrawUseCaseTest {
    private AccountDatabaseRepository accountRepository;
    private WithdrawUseCase withdrawUseCase;

    @BeforeEach
    void setUp() {
        this.accountRepository = mock(AccountDatabaseRepository.class);
        this.withdrawUseCase = new WithdrawUseCase(accountRepository);
    }

    @Test
    @DisplayName("Deve realizar saque com sucesso")
    void shouldDepositAmount() throws Exception {
        var account = new Account (
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        account.depositAmount(AssetEnum.BTC, 150.0);

        var balanceDto = new BalanceDTO("BTC", 100.0);

        when(accountRepository.get(account.getId())).thenReturn(account);

        withdrawUseCase.execute(account.getId(), balanceDto);
        assertEquals(account.getBalance(AssetEnum.BTC).getAmount(), 50.0);
    }

    @Test
    @DisplayName("Não deve realizar saque se o saldo do ativo não for insuficiente")
    void shouldThrowExceptionWhenWithdrawAmountWithIsufficientFunds() {
        var account = new Account (
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        account.depositAmount(AssetEnum.BTC, 50.0);

        var balanceDto = new BalanceDTO("BTC", 100.0);

        when(accountRepository.get(account.getId())).thenReturn(account);

        var exception = assertThrows(Exception.class, () -> 
            withdrawUseCase.execute(account.getId(), balanceDto)
        );

        assertEquals(exception.getMessage(), "Insufficient funds");
    }

    @Test
    @DisplayName("Não deve realizar saque se o ativo não existir na conta")
    void shouldThrowExceptionWhenWithdrawAmountInAccountWithoutThatAsset() {
        var account = new Account (
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        account.depositAmount(AssetEnum.BTC, 50.0);

        var balanceDto = new BalanceDTO("USD", 100.0);

        when(accountRepository.get(account.getId())).thenReturn(account);

        var exception = assertThrows(Exception.class, () -> 
            withdrawUseCase.execute(account.getId(), balanceDto)
        );

        assertEquals(exception.getMessage(), "Insufficient funds");
    }
}
