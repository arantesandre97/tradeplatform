package org.mypersonalprojects.tradeplatform.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mypersonalprojects.tradeplatform.domain.Account;
import org.mypersonalprojects.tradeplatform.infra.dto.AccountDTO;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountDatabaseRepository;

public class CreateAccountUseCaseTest {
    private AccountDatabaseRepository accountRepository;
    private CreateAccountUseCase createAccountUseCase;

    @BeforeEach
    void setUp() {
        this.accountRepository = mock(AccountDatabaseRepository.class);
        this.createAccountUseCase = new CreateAccountUseCase(accountRepository);
    }

    @Test
    @DisplayName("Deve criar uma conta com sucesso")
    void shouldCreateAnAccount() {
        AccountDTO accountDto = new AccountDTO(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123",
            new HashMap<>()
        );

        UUID returnedId = createAccountUseCase.execute(accountDto);

        assertNotNull(returnedId);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertEquals(returnedId, capturedAccount.getId());
    }

    @Test
    @DisplayName("Não deve criar uma conta com algum atributo invalido")
    void shouldThrowExceptionWhenCreateAccount() {
        AccountDTO accountDto = new AccountDTO(
            "John Doe", 
            "john.doe@example.com", 
            "62573679050", 
            "Password@123",
            new HashMap<>()
        );
        
        var exception = assertThrows(IllegalArgumentException.class, () -> createAccountUseCase.execute(accountDto));
        assertEquals("Invalid document", exception.getMessage());
    }   
}
