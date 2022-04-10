package com.senpure.cli;

import javax.annotation.Nonnull;

public abstract class AbstractCommandApplication  implements CommandApplication{


    @Nonnull
    protected  abstract RootCommand rootCommand();

    public abstract void start();

}
