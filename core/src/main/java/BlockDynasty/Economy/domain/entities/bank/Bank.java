package BlockDynasty.Economy.domain.entities.bank;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/*
* crear banco(string banco) :void

borrar banco(string banco) : void

el banco tiene(string banco,double monto): boolean  //vaul del banco//si tiene dinero

extraer de banco(string banco, double monto) : void

depositar en banco(string,double monto) : void

es dueño del banco(string banco,string playerNmae) : boolean

balance del banco(string nombre): double

obtener bancos(): lista de bancos
* */
public class Bank {
    private String name;
    private Player owner;
    private List<Money> vault;

    public Bank(String name,UUID ouwnerUUID, String owner) {
        this.name = name;
        this.owner = new Player(owner, ouwnerUUID.toString());
        this.vault = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addBalance(Money money) {
        this.vault.add(money);
    }

    public void setVault(List<Money> monies) {
        this.vault = monies;
    }

    public String getOwnerName() {
        return owner.getNickname();
    }

    public String getOwnerUUID() {
        return owner.getUuid();
    }

    public BigDecimal getBalance(Currency currency) {
        for (Money money : vault) {
            if (money.getCurrency().equals(currency)) {
                return money.getAmount();
            }
        }
        return BigDecimal.ZERO; // Return zero if no balance found for the currency
    }

    public void deposit(Currency currency,BigDecimal amount) {
        for (Money money : vault) {
            if (money.getCurrency().equals(currency)) {
                money.setAmount(money.getAmount().add(amount));
                return;
            }
        }
    }

    public void withdraw(Currency currency,BigDecimal amount) {
        for (Money money : vault) {
            if (money.getCurrency().equals(currency)) {
                BigDecimal newBalance = money.getAmount().subtract(amount);
                if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
                    money.setAmount(newBalance);
                }
                return;
            }
        }
    }
}
