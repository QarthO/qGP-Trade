package gg.quartzdev.qgptrade.commands;

import gg.quartzdev.lib.qlibpaper.Sender;
import gg.quartzdev.lib.qlibpaper.commands.QCommand;
import gg.quartzdev.lib.qlibpaper.lang.QMessage;
import gg.quartzdev.qgptrade.TradeAPI;
import gg.quartzdev.qgptrade.storage.Config;
import gg.quartzdev.qgptrade.transaction.Transaction;
import gg.quartzdev.qgptrade.util.Args;
import gg.quartzdev.qgptrade.util.Messages;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CMDwithdraw extends QCommand {
    Config config;

    public CMDwithdraw(String commandName, String permissionGroup) {
        super(commandName, permissionGroup);
        config = TradeAPI.getConfig();
    }

    @Override
    public boolean logic(CommandSender sender, String label, String[] args) {

//        Gets player
        if(!(sender instanceof Player player)) {
            Sender.message(sender, Messages.ERROR_PLAYER_ONLY_COMMAND);
            return false;
        }

//        Checks syntax
        if(args.length != 2){
            Sender.message(sender, Messages.SYNTAX_WITHDRAW.parse("label", label));
            return false;
        }

//        Loads player's claim blocks
        PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());
        if(playerData == null){
            Sender.message(sender, Messages.ERROR_WITHDRAW_LOAD_CLAIM_BLOCKS);
            return false;
        }

        int blocksToWithdraw = Args.parseInt(args[1]);
        int tax = 0;

        if(blocksToWithdraw == 0){
            Sender.message(sender,
                    Messages.ERROR_WITHDRAW_INVALID_NUMBER.parse("input", args[1])
            );
            return false;
        }

        if(blocksToWithdraw < config.getWithdrawMin() + tax){
            Sender.message(sender,
                    Messages.ERROR_WITHDRAW_INVALID_NUMBER_MIN.parse("blocks", String.valueOf(config.getWithdrawMin() + tax)));
            return false;
        }

        if(blocksToWithdraw > config.getWithdrawMax() - tax){
            Sender.message(sender,
                    Messages.ERROR_WITHDRAW_INVALID_NUMBER_MAX);
            return false;
        }

        int blocksAvailable = playerData.getRemainingClaimBlocks();
        int blocksRemaining = blocksAvailable - blocksToWithdraw;

        if(blocksRemaining < 0){
            Sender.message(player, Messages.ERROR_WITHDRAW_NOT_ENOUGH_CLAIM_BLOCKS);
            return false;
        }

        int blocksBonus = playerData.getBonusClaimBlocks();
        int blocksAccrued = playerData.getAccruedClaimBlocks();

        if(blocksBonus >= blocksToWithdraw){
            playerData.setBonusClaimBlocks(blocksBonus - blocksToWithdraw);
        } else {
            playerData.setAccruedClaimBlocks(blocksAccrued - blocksToWithdraw);
        }
        GriefPrevention.instance.dataStore.savePlayerData(player.getUniqueId(), playerData);

        Transaction transaction = TradeAPI.getTransactionManager().createTransaction(player, blocksToWithdraw);
        player.getInventory().addItem(transaction.slip());

        QMessage successResponse = Messages.WITHDRAW_CLAIMBLOCKS
                .parse("blocks_withdraw", String.valueOf(blocksToWithdraw))
                .parse("blocks_remaining", String.valueOf(blocksRemaining));
        Sender.message(player, successResponse);
        return true;
    }

    @Override
    public Iterable<String> tabCompletionLogic(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            completions.add("<amount>");
            return completions;
        }
        return completions;
    }
}
