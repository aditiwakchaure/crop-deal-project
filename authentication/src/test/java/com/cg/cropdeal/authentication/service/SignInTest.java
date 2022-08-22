package com.cg.cropdeal.authentication.service;

import com.cg.cropdeal.authentication.dao.IAccountRepository;
import com.cg.cropdeal.authentication.exception.InvalidCredentialsException;
import com.cg.cropdeal.authentication.exception.UserNotFoundException;
import com.cg.cropdeal.authentication.model.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith (MockitoExtension.class)
public class SignInTest {
	@InjectMocks
	AccountServiceImpl service = new AccountServiceImpl();

	@Mock
	IAccountRepository repository;

	Account account;

	@BeforeEach
	void setUp () {
		account = new Account();
	}

	//	********** testing InvalidCredentialsException *******************
	@Test
	@DisplayName ("Test empty email")
	void testEmptyEmail () {
		account.setPassword("test");
		Assertions.assertThrows(InvalidCredentialsException.class, () -> service.signInWithEmail(account), "should throw invalid credentials exception");
	}

	@Test
	@DisplayName ("Test empty password")
	void testEmptyPassword () {
		account.setEmail("test");
		Assertions.assertThrows(InvalidCredentialsException.class, () -> service.signInWithEmail(account), "should throw invalid credentials exception");
	}

	// ********* testing UserNotFoundException exception **************
	@Test
	@DisplayName ("Test UserNotFoundException exception")
	void testUserNotFoundException () {
		account.setEmail("test");
		account.setPassword("test");

		Assertions.assertThrows(UserNotFoundException.class,
		 () -> service.signInWithEmail(account));
	}

	//	********** testing successful sign in with email functionality **********
	@Test
	@DisplayName ("Test Signin with email functionality")
	void testSignInWithEmail () {
		account.setEmail("test");
		account.setPassword("test");
		account.setFullName("test");

		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(account);
		Account returnedObj = service.signInWithEmail(account);
		Assertions.assertAll(
		 () -> Assertions.assertEquals(returnedObj.getEmail(), account.getEmail()),
		 () -> Assertions.assertNull(returnedObj.getPassword()),
		 () -> Assertions.assertEquals(returnedObj.getFullName(), account.getFullName())
		);
	}
}

