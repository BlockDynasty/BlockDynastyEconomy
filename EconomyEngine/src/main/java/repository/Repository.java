/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package repository;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.Exceptions.AccountAlreadyExist;
import BlockDynasty.Economy.domain.entities.account.Exceptions.AccountNotFoundException;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.persistence.Exceptions.RepositoryException;
import BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import BlockDynasty.Economy.domain.persistence.entities.ICurrencyRepository;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.persistence.transaction.ITransactions;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.result.TransferResult;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import repository.ConnectionHandler.Hibernate.Connection;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Repository extends TransactionRepository implements IRepository {
    private Connection connection;
    private final SessionFactory sessionFactory;
    private IAccountRepository accountRepository;
    private ICurrencyRepository currencyRepository;

    public Repository(Connection connection) {
        super(connection.getSession());
        this.connection = connection;
        this.sessionFactory = connection.getSession();
        this.accountRepository = new AccountRepository(sessionFactory);
        this.currencyRepository = new CurrencyRepository(sessionFactory);
    }

    @Override
    public List<ICurrency> loadCurrencies() {
        return currencyRepository.findAll();
    }

    public Result<ICurrency> loadCurrencyByName(String name) {
        try {
            ICurrency currency = currencyRepository.findByName(name);
            return Result.success(currency);
        } catch (CurrencyNotFoundException e) {
            return Result.failure("Currency with name " + name + " not found.", ErrorCode.CURRENCY_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error loading currency: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    public Result<ICurrency> loadCurrencyByUuid(String uuid) {
        try {
            ICurrency currency = currencyRepository.findByUuid(uuid);
            return Result.success(currency);
        } catch (CurrencyNotFoundException e) {
            return Result.failure("Currency with UUID " + uuid + " not found.", ErrorCode.CURRENCY_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error loading currency: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    public Result<ICurrency> loadDefaultCurrency() {
        try {
            ICurrency currency = currencyRepository.findDefaultCurrency();
            return Result.success(currency);
        } catch (CurrencyNotFoundException e) {
            return Result.failure("Default currency not found.", ErrorCode.CURRENCY_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error loading default currency: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public void saveCurrency(ICurrency currency) {
        try {
            currencyRepository.save(currency);
        }catch (CurrencyNotFoundException e) {
            currencyRepository.create(currency);
        } catch (Exception e) {
            throw new RuntimeException("Error saving currency: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCurrency(ICurrency currency) {
        try {
            currencyRepository.delete(currency);
        }catch (CurrencyNotFoundException e) {
            //
        }catch (Exception e) {
            //
        }

    }

    @Override
    public List<Account> loadAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Result<Account> loadAccountByUuid(UUID uuid) {
        try{
            Account account =accountRepository.findByUuid(uuid);
            return Result.success(account);
        }catch (AccountNotFoundException e) {
            return Result.failure("Account with UUID " + uuid + " not found.",ErrorCode.ACCOUNT_NOT_FOUND);
        }catch (NonUniqueResultException e) {
            return Result.failure("Multiple accounts found with UUID " + uuid + ".",ErrorCode.ACCOUNT_DUPLICATED);
        } catch (RepositoryException e) {
            return Result.failure("Error loading account: " + e.getMessage(),ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> loadAccountByName(String name) {
        try{
            Account account = accountRepository.findByNickname(name);
            return Result.success(account);
        }catch (AccountNotFoundException e) {
            return Result.failure("Account with name " + name + " not found.", ErrorCode.ACCOUNT_NOT_FOUND);
        }catch (NonUniqueResultException e) {
            return Result.failure("Multiple accounts found with name " + name + ".", ErrorCode.ACCOUNT_DUPLICATED);
        }catch (RepositoryException e) {
            return Result.failure("Error loading account: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public Result<Account> loadAccountByPlayer(Player player) {
        try{
            Account account = accountRepository.findByPlayer(player);
            return Result.success(account);
        }catch (AccountNotFoundException e) {
            return Result.failure("Account for player " + player.getNickname() + " not found.", ErrorCode.ACCOUNT_NOT_FOUND);
        }catch (NonUniqueResultException e) {
            return Result.failure("Multiple accounts found for player " + player.getNickname() + ".", ErrorCode.ACCOUNT_DUPLICATED);
        }catch (RepositoryException e) {
            return Result.failure("Error loading account: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public void createAccount(Account account) {
        try {
            accountRepository.create(account);
        }catch (AccountAlreadyExist e){//
            //
        }
        catch (Exception e) {
            //
        }
    }

    @Override
    public void saveAccount(Account account) {
        try {
            accountRepository.save(account);
        }catch (AccountNotFoundException e) {
            accountRepository.create(account);
        }catch (Exception e) {
            throw new RepositoryException( "Error saving account: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveAccount(Player player, Account account) {
        try {
            accountRepository.save(player,account);
        }catch (AccountNotFoundException e) {
            accountRepository.create(account);
        }catch (Exception e) {
            throw new RepositoryException( "Error saving account: " + e.getMessage(), e);
        }
    }

    @Override
    public Result<Void> deleteAccount(Player account) {
        try {
            accountRepository.delete(account);
            return Result.success(null);
        } catch (AccountNotFoundException e) {
            return Result.failure( "Account with UUID " + account.getUuid() + " not found.",ErrorCode.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            return Result.failure("Error deleting account: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
        }
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        return accountRepository.getAccountsTopByCurrency( currencyName, limit, offset );
    }

    @Override
    public boolean isTopSupported() {
        return true;
    }

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public void close() {
        connection.close();
    }

    @Override
    public void clearAll() {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            try {
                // Delete in correct order to respect foreign key constraints

                // First remove balances (which reference wallets and currencies)
                session.createQuery("DELETE FROM BalanceDb").executeUpdate();

                // Then remove accounts (which reference wallets)
                session.createQuery("DELETE FROM AccountDb").executeUpdate();

                // Now it's safe to remove wallets as no accounts reference them
                session.createQuery("DELETE FROM WalletDb").executeUpdate();

                // Finally currencies can be removed
                session.createQuery("DELETE FROM CurrencyDb").executeUpdate();

                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error clearing database: " + e.getMessage(), e);
            }
        }
    }
}
