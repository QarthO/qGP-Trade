package gg.quartzdev.qgptrade.util;

import gg.quartzdev.lib.qlibpaper.QLogger;
import gg.quartzdev.lib.qlibpaper.lang.GenericMessages;
import gg.quartzdev.lib.qlibpaper.lang.QMessage;
import gg.quartzdev.qgptrade.TradeAPI;
import gg.quartzdev.qgptrade.storage.YMLmessages;

public class Messages extends GenericMessages {
    private static Messages INSTANCE;
    YMLmessages messagesFile;

//    WITHDRAW CLAIMBLOCKS
    public static QMessage SYNTAX_WITHDRAW = new QMessage("<prefix> <red>Syntax: /<label> withdraw <amount>");
    public static QMessage WITHDRAW_CLAIMBLOCKS = new QMessage("" +
            "<prefix> <blue>You withdrew <yellow><blocks_withdraw> <blue>claim blocks<newline>" +
            "<prefix> <blue>You now have <yellow><blocks_remaining> <blue>remaining");
    public static QMessage ERROR_WITHDRAW_NOT_ENOUGH_CLAIM_BLOCKS = new QMessage("" +
            "<prefix> <red>Error: You don't have enough claim blocks");
    public static QMessage ERROR_WITHDRAW_LOAD_CLAIM_BLOCKS = new QMessage("" +
            "<prefix> <red>Error: Unable to load your claim blocks data from GriefPrevention");
    public static QMessage ERROR_WITHDRAW_INVALID_NUMBER = new QMessage("" +
            "<prefix> <red>Error: You must withdraw at least <blocks_minimum> claim blocks");

    public static QMessage DEPOSIT_CLAIM_BLOCKS = new QMessage("" +
            "<prefix> <blue>You deposited <yellow><blocks_deposit> <blue>claim blocks<newline>" +
            "<prefix> <blue>You now have <yellow><blocks_remaining> <blue>available claim blocks");


    public static QMessage VAULT_HOOKED = new QMessage("<prefix> <green>Successfully hooked into <yellow>Vault's<green> Economy");
    public static QMessage ERROR_VAULT_HOOK = new QMessage("<prefix> <red>Error: Vault found, but unable to hook into it's economy");
    public static QMessage WARNING_VAULT_NOT_FOUND = new QMessage("<prefix> <yellow> Warning: Vault is required for the economy integration");
    private Messages(String consolePrefix, String chatPrefix){
        super(consolePrefix, chatPrefix);
        messagesFile = new YMLmessages(TradeAPI.getPlugin(), "messages.yml");
    }
    public static void init(String consolePrefix, String chatPrefix){
        if(INSTANCE != null){
            return;
        }
        INSTANCE = new Messages(consolePrefix, chatPrefix);
        System.out.println("=================");
        System.out.println("=================");
        System.out.println("=================");
        System.out.println(CONSOLE_PREFIX.get());
        System.out.println(CHAT_PREFIX.get());
        System.out.println("=================");
        System.out.println("=================");
        System.out.println("=================");
    }


}
