package com.uaito.test;

import br.com.six2six.fixturefactory.Fixture;
import com.uaito.controllers.AccountController;
import com.uaito.domain.Account;
import com.uaito.enuns.YesNoEnum;
import com.uaito.exception.NotFoundException;
import com.uaito.fixture.AccountFixture;
import com.uaito.fixture.FixtureLoader;
import com.uaito.repository.AccountRepository;
import com.uaito.service.AccountService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;


public class AccountTest {

    @BeforeClass
    public static void setupTest() {
        FixtureLoader.loadTemplates();
    }

    @Test
    public void test() {
        Account valid = Fixture.from(Account.class).gimme(AccountFixture.VALID);

        Assert.assertTrue(valid.getEmail().equals(valid.getFirstName()+"."+valid.getLastName()+"@email.com"));

    }

    @Test
    public void testInactive() {
        Account account = Fixture.from(Account.class).gimme(AccountFixture.INACTIVE);

        Assert.assertTrue(YesNoEnum.NO.equals(account.getActive()));

    }

    @Test
    public void testNotFound() throws NotFoundException {

        AccountController accountController = new AccountController();

        AccountService accountService = Mockito.mock(AccountService.class);

        Mockito.when(accountService.findByIdOrEmail(Mockito.anyString())).thenCallRealMethod();

        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);

        ReflectionTestUtils.setField(accountController,"accountService", accountService);
        ReflectionTestUtils.setField(accountService,"accountRepository", accountRepository);

        ResponseEntity<?> email = accountController.get("email@email.com");

        Assert.assertTrue(email.getStatusCode().is4xxClientError());

    }

}
