package com.example.sheahtaa.popularmovie;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by Sheahtaa on 8/31/2016.
 */


@SimpleSQLConfig(
        name = "MovieProvider",
        authority = "just.some.test_provider.authority",
        database = "movies.db",
        version = 5)
public class TestProviderConfig implements ProviderConfig {
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
