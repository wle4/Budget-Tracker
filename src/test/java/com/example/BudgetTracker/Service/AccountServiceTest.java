package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.DTO.AccountRequestDTO;
import com.example.BudgetTracker.DTO.AccountResponseDTO;
import com.example.BudgetTracker.Entity.Account;
import com.example.BudgetTracker.Entity.User;
import com.example.BudgetTracker.Repository.AccountRepository;
import com.example.BudgetTracker.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AccountService accountService;

    @Test
    void createAccount_ValidUser_ReturnsAccountResponseDTO() {
        // arrange
        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setBalance(BigDecimal.valueOf(1000));
        dto.setName("TestName");
        dto.setType("TestType");

        User user = new User();
        user.setEmail("example@gmail.com");
        user.setId(UUID.randomUUID());

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(1000));
        account.setName("TestName");
        account.setType("TestType");
        account.setUser(user);
        account.setId(UUID.randomUUID());

        // tell
        when(userRepository.findByEmail(account.getUser().getEmail()))
                .thenReturn(Optional.of(user));

        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        // act
        AccountResponseDTO response = accountService.createAccount(dto, user.getEmail());

        // assert
        assertNotNull(response);
        assertEquals("TestName", response.getName());
        assertEquals("TestType", response.getType());
        assertEquals(BigDecimal.valueOf(1000), response.getBalance());
        assertEquals(user.getId(), response.getUserId());

        // verify
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_InvalidUser_ThrowsException() {
        // arrange
        String invalidEmail = "testemail@gmail.com";

        AccountRequestDTO dto = new AccountRequestDTO();

        // tell
        when(userRepository.findByEmail(invalidEmail))
                .thenReturn(Optional.empty());

        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
           accountService.createAccount(dto, invalidEmail);
        });

        // assert
        assertEquals("User not found", exception.getMessage());

        // verify
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void getAccountsByUserId_ValidAccounts_ReturnsAccountList() {
        // arrange
        User registeredUser = new User();
        registeredUser.setId(UUID.randomUUID());
        registeredUser.setEmail("test@gmail.com");
        registeredUser.setFullName("Test User");

        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setUser(registeredUser);
        account.setType("Checking");
        account.setName("My Account");
        account.setBalance(BigDecimal.valueOf(1000));

        List<Account> testAccountList = List.of(account);

        // tell
        when(accountRepository.findByUserId(registeredUser.getId()))
                .thenReturn(testAccountList);

        // act
        List<AccountResponseDTO> result = accountService.getAccountsByUserId(registeredUser.getId());

        // assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("My Account", result.getFirst().getName());
        assertEquals("Checking", result.getFirst().getType());
        assertEquals(registeredUser.getId(), result.getFirst().getUserId());

        // verify
        verify(accountRepository, times(1)).findByUserId(registeredUser.getId());
    }

    @Test
    void getAccountsByUserId_AccountListEmpty_ThrowsException() {
        // arrange
        User registeredUser = new User();
        registeredUser.setId(UUID.randomUUID());
        registeredUser.setEmail("test@gmail.com");
        registeredUser.setFullName("Test User");

        // tell
        when(accountRepository.findByUserId(registeredUser.getId()))
                .thenReturn(List.of()); // empty list
        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.getAccountsByUserId(registeredUser.getId());
        });

        //assert
        assertEquals("No accounts registered with this user", exception.getMessage());

        // verify
        verify(accountRepository, times(1)).findByUserId(registeredUser.getId());
    }

    @Test
    void deleteAccount_ValidAccountId_DeletesAccount() {
        // arrange
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setType("Checking");
        account.setName("My Account");
        account.setBalance(BigDecimal.valueOf(1000));

        // tell
        when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.of(account));

        // act
        accountService.deleteAccount(account.getId());

        // verify
        verify(accountRepository, times(1)).deleteById(account.getId());
    }

    @Test
    void deleteAccount_InvalidAccountId_ThrowsException() {
        // arrange
        UUID id = UUID.randomUUID();

        // tell
        when(accountRepository.findById(id))
                .thenReturn(Optional.empty());

        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.deleteAccount(id);
        });

        // assert
        assertEquals("Account not found", exception.getMessage());

        // verify deleteById was NEVER called since it threw before reaching it
        verify(accountRepository, never()).deleteById(id);
    }


}
