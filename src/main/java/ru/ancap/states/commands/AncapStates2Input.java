package ru.ancap.states.commands;

import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleHandle;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleReloadInput;

public class AncapStates2Input extends CommandTarget {
    
    public AncapStates2Input(LocaleHandle localeHandle) {
        super(new Delegate(
            new SubCommand("reload", new Delegate(
                new SubCommand("locales", new LocaleReloadInput(localeHandle))
            ))
        ));
    }
    
}