package com.uaito.fixture;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

public class FixtureLoader {

    public static void loadTemplates() {
        FixtureFactoryLoader.loadTemplates(FixtureLoader.class.getPackage().getName());
    }
}
