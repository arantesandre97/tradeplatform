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
import org.mypersonalprojects.tradeplatform.infra.repository.AccountDatabaseRepository;

public class GetAccountUseCaseTest {
    private AccountDatabaseRepository accountRepository;
    private GetAccountUseCase getAccountUseCase;

    @BeforeEach
    void setUp() {
        this.accountRepository = mock(AccountDatabaseRepository.class);
        this.getAccountUseCase = new GetAccountUseCase(accountRepository);
    }

    @Test
    @DisplayName("Deve retornar uma conta")
    void shouldReturnAnAccount() {
        var account = new Account (
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        when(accountRepository.get(account.getId())).thenReturn(account);

        var returnedAccount = getAccountUseCase.execute(account.getId());
        assertEquals(account, returnedAccount);
    }

    @Test
    @DisplayName("Não deve retornar uma conta quando não existir")
    void shouldReturnAccountNotFound(){
        when(accountRepository.get(any(UUID.class))).thenThrow(new RuntimeException("Account not found"));

        var exception = assertThrows(RuntimeException.class, () -> getAccountUseCase.execute(UUID.randomUUID()));
        assertEquals("Account not found", exception.getMessage());
    }
}
