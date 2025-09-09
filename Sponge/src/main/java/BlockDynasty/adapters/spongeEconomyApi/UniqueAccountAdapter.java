package BlockDynasty.adapters.spongeEconomyApi;
import BlockDynasty.Economy.domain.entities.balance.Money;
import api.EconomyResponse;
import api.IApi;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UniqueAccountAdapter implements UniqueAccount{
    private BlockDynasty.Economy.domain.entities.account.Account account;
    private IApi api;

    public UniqueAccountAdapter(BlockDynasty.Economy.domain.entities.account.Account account, IApi api) {
        this.account = account;
        this.api = api;
    }

    @Override
    public Component displayName() {
        return Component.text(account.getNickname());
    }

    @Override
    public BigDecimal defaultBalance(Currency currency) {
        return api.getBalance(account.getUuid());
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> contexts) {
        try {
            api.getBalance(account.getUuid());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean hasBalance(Currency currency, Cause cause) {
        try {
            api.getBalance(account.getUuid());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public BigDecimal balance(Currency currency, Set<Context> contexts) {
        try {
            return api.getBalance(account.getUuid());
        }catch (Exception e){
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal balance(Currency currency, Cause cause) {
        try {
            return api.getBalance(account.getUuid());
        }catch (Exception e){
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Map<Currency, BigDecimal> balances(Set<Context> contexts) {
        List<Money> moneyList = account.getWallet().getBalances();
        Map<Currency, BigDecimal> balances = new java.util.HashMap<>();
        for(Money money : moneyList){
            balances.put(new CurrencyAdapter(money.getCurrency()), money.getAmount());
        }
        return balances;
    }

    @Override
    public Map<Currency, BigDecimal> balances(Cause cause) {
        List<Money> moneyList = account.getWallet().getBalances();
        Map<Currency, BigDecimal> balances = new java.util.HashMap<>();
        for(Money money : moneyList){
            balances.put(new CurrencyAdapter(money.getCurrency()), money.getAmount());
        }
        return balances;
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Set<Context> contexts) {
        EconomyResponse response = api.setBalance(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.DEPOSIT.get());
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause) {
        EconomyResponse response = api.setBalance(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.DEPOSIT.get());
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Set<Context> contexts) {
        return Map.of();
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Cause cause) {
        return Map.of();
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Set<Context> contexts) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause) {
        return null;
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Set<Context> contexts) {
        EconomyResponse response = api.deposit(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.DEPOSIT.get());
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
        EconomyResponse response = api.deposit(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.DEPOSIT.get());
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Set<Context> contexts) {
        EconomyResponse response = api.withdraw(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount,TransactionTypes.WITHDRAW.get());
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
        EconomyResponse response = api.withdraw(account.getUuid(), amount, PlainTextComponentSerializer.plainText().serialize(currency.displayName()));
        return new TransactionResultAdapter(response, currency, this, amount, TransactionTypes.WITHDRAW.get());
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Set<Context> contexts) {
        EconomyResponse response = api.transfer(this.identifier(),to.identifier(), PlainTextComponentSerializer.plainText().serialize(currency.displayName()),amount);
        return new TransferResultAdapter(response, currency, this, to, amount);
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause) {
        EconomyResponse response = api.transfer(this.identifier(),to.identifier(), PlainTextComponentSerializer.plainText().serialize(currency.displayName()),amount);
        return new TransferResultAdapter(response, currency, this, to, amount);
    }

    @Override
    public String identifier() {
        return account.getNickname();
    }

    @Override
    public UUID uniqueId() {
        return account.getUuid();
    }
}
