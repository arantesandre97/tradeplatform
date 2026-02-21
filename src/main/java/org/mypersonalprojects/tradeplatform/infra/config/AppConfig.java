package org.mypersonalprojects.tradeplatform.infra.config;

import org.mypersonalprojects.tradeplatform.application.usecase.CreateAccountUseCase;
import org.mypersonalprojects.tradeplatform.application.usecase.DepositUseCase;
import org.mypersonalprojects.tradeplatform.application.usecase.GetAccountUseCase;
import org.mypersonalprojects.tradeplatform.application.usecase.WithdrawUseCase;
import org.mypersonalprojects.tradeplatform.infra.controller.AccountController;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountDatabaseRepository;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {
    @Bean
    public AccountRepository accountRepository(JdbcTemplate jdbcTemplate) {
        return new AccountDatabaseRepository(jdbcTemplate);
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountRepository accountRepository) {
        return new CreateAccountUseCase(accountRepository);
    }

    @Bean
    public GetAccountUseCase getAccountUseCase(AccountRepository accountRepository) {
        return new GetAccountUseCase(accountRepository);
    }

    @Bean
    public DepositUseCase depositUseCase(AccountRepository accountRepository) {
        return new DepositUseCase(accountRepository);
    }

    @Bean
    public WithdrawUseCase withdrawUseCase(AccountRepository accountRepository) {
        return new WithdrawUseCase(accountRepository);
    }

    @Bean
    public AccountController accountController(AccountRepository accountRepository) {
        return new AccountController(accountRepository);
    }
}
