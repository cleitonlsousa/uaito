package com.uaito.fixture;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.uaito.domain.Account;
import com.uaito.enuns.YesNoEnum;

public class AccountFixture implements TemplateLoader {

    public final static String VALID = "valid";
    public final static String INACTIVE = "inactive";

    @Override
    public void load() {

        Fixture.of(Account.class).addTemplate(VALID, new Rule(){
            {
                add("id", random(Long.class, range(1L, 999999L)));
                add("firstName", "john");
                add("lastName", "doe");
                add("email", "${firstName}.${lastName}@email.com");
                add("password", "123456");
                add("active", YesNoEnum.YES);
            }
        }).addTemplate(INACTIVE).inherits(VALID, new Rule() {
            {
                add("active", YesNoEnum.NO);
            }
        });
    }

}
