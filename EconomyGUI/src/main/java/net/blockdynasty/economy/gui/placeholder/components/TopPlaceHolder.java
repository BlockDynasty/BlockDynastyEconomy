package net.blockdynasty.economy.gui.placeholder.components;

import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetTopAccountsUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.entities.balance.Money;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.services.messages.MessageService;
import net.blockdynasty.economy.libs.util.colors.ChatColor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TopPlaceHolder {
    private final GetTopAccountsUseCase getTopAccountsUseCase;

    public TopPlaceHolder(GetTopAccountsUseCase getTopAccountsUseCase) {
        this.getTopAccountsUseCase = getTopAccountsUseCase;
    }

    public String handle(String placeholder) {
        String[] parts = placeholder.split("_");
        if (parts.length < 3) return "Invalid placeholder format";

        String currencyName = parts[1];
        int limit;
        int position = -1;

        try {
            limit = Integer.parseInt(parts[2]);
            if (parts.length >= 4) {
                position = Integer.parseInt(parts[3]) - 1;
            }
        } catch (NumberFormatException e) {
            return "Invalid number format";
        }

        Result<List<Account>> result = getTopAccountsUseCase.execute(currencyName, limit, 0);
        if (!result.isSuccess()) return "No accounts found";

        List<Account> accounts = result.getValue();

        if (position >= 0) {
            if (position >= accounts.size()) return "Position out of range";
            return formatTopAccount(position, accounts.get(position), currencyName);
        }

        return IntStream.range(0, accounts.size())
                .mapToObj(i -> formatTopAccount(i, accounts.get(i), currencyName))
                .collect(Collectors.joining("\n"));
    }

    private String formatTopAccount(int index, Account account, String currencyName) {
        Money money = account.getMoney(currencyName);
        return MessageService.getMessage("balance_top.balance", Map.of(
                "number", String.valueOf(index + 1),
                "currencycolor", ChatColor.formatColorToPlaceholder(money.getCurrency().getColor()),
                "player", account.getNickname(),
                "balance", money.format()
        ));
    }
}