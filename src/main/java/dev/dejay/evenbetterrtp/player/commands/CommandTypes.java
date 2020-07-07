package dev.dejay.evenbetterrtp.player.commands;

import dev.dejay.evenbetterrtp.player.commands.types.CmdBiome;
import dev.dejay.evenbetterrtp.player.commands.types.CmdHelp;
import dev.dejay.evenbetterrtp.player.commands.types.CmdInfo;
import dev.dejay.evenbetterrtp.player.commands.types.CmdPlayer;
import dev.dejay.evenbetterrtp.player.commands.types.CmdReload;
import dev.dejay.evenbetterrtp.player.commands.types.CmdVersion;
import dev.dejay.evenbetterrtp.player.commands.types.CmdWorld;

public enum CommandTypes {
    BIOME(new CmdBiome()),
    HELP(new CmdHelp()),
    INFO(new CmdInfo()),
    PLAYER(new CmdPlayer()),
    RELOAD(new CmdReload()),
    VERSION(new CmdVersion()),
    WORLD(new CmdWorld());

    private RTPCommand cmd;

    CommandTypes(RTPCommand cmd) {
        this.cmd = cmd;
    }

    public RTPCommand getCommands() {
        return cmd;
    }
}
