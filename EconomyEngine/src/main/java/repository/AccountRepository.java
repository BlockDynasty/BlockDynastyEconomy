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
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.RepositoryException;
import BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.Mappers.AccountMapper;
import repository.Models.AccountDb;
import repository.Models.BalanceDb;
import repository.Models.CurrencyDb;
import repository.Models.WalletDb;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountRepository implements IAccountRepository {
    @PersistenceContext
    private SessionFactory sessionFactory;

    public AccountRepository( SessionFactory sessionFactory) {
        this.sessionFactory =sessionFactory;
    }

    @Override
    public List<Account> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                List<Account> accounts = session.createQuery(
                                "SELECT DISTINCT a FROM AccountDb a" , AccountDb.class)
                        .getResultList()
                        .stream()
                        .map(AccountMapper::toDomain)
                        .collect(Collectors.toList());
                tx.commit();
                return accounts;
            }catch (Exception e) {
                tx.rollback();
                throw new RepositoryException( "Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Account findByUuid(String uuid) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "LEFT JOIN FETCH a.wallet w " +
                                        "LEFT JOIN FETCH w.balances b " +
                                        "LEFT JOIN FETCH b.currency " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", uuid)
                        .getSingleResult();
                tx.commit();
                return AccountMapper.toDomain(accountDb);
            } catch (NoResultException e) {
                tx.rollback();
                throw new AccountNotFoundException("Account no encontrado: " + uuid);
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Account findByNickname(String nickname) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "LEFT JOIN FETCH a.wallet w " +
                                        "LEFT JOIN FETCH w.balances b " +
                                        "LEFT JOIN FETCH b.currency " +
                                        "WHERE a.nickname = :name",
                                AccountDb.class)
                        .setParameter("name", nickname)
                        .getSingleResult();
                tx.commit();
                return AccountMapper.toDomain(accountDb);
            } catch (NoResultException e) {
                tx.rollback();
                throw new AccountNotFoundException(e.getMessage());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void save(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a LEFT JOIN FETCH a.wallet w LEFT JOIN FETCH w.balances b LEFT JOIN FETCH b.currency WHERE a.uuid = :uuid OR a.nickname = :name",
                                AccountDb.class)
                        .setParameter("uuid", account.getUuid().toString())
                        .setParameter("name", account.getNickname())
                        .getSingleResult();

                // Update basic properties
                accountDb.setNickname(account.getNickname());
                accountDb.setUuid(account.getUuid().toString());
                accountDb.setCanReceiveCurrency(account.canReceiveCurrency());
                accountDb.setBlock(account.isBlocked());

                // Use the helper method from TransactionRepository for balance updates
                updateBalancesInWallet(account, accountDb.getWallet(), session);

                // Hibernate will automatically detect and persist changes
                tx.commit();
            } catch (NoResultException e) {
                tx.rollback();
                throw new AccountNotFoundException("Account no encontrado: " + account.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }
    // Helper method similar to what's in TransactionRepository
    private void updateBalancesInWallet(Account account, WalletDb walletDb, Session session) {
        for (Money money : account.getBalances()) {
            String currencyUuid = money.getCurrency().getUuid().toString();

            // Find existing balance by currency
            Optional<BalanceDb> existingBalance = walletDb.getBalances().stream()
                    .filter(b -> b.getCurrency().getUuid().equals(currencyUuid))
                    .findFirst();

            if (existingBalance.isPresent()) {
                // Update existing balance amount
                existingBalance.get().setAmount(money.getAmount());
            } else {
                // Create new balance
                try {
                    CurrencyDb currencyDb = session.createQuery(
                                    "FROM CurrencyDb WHERE uuid = :uuid", CurrencyDb.class)
                            .setParameter("uuid", currencyUuid)
                            .getSingleResult();

                    BalanceDb newBalance = new BalanceDb();
                    newBalance.setCurrency(currencyDb);
                    newBalance.setAmount(money.getAmount());
                    newBalance.setWallet(walletDb);
                    walletDb.getBalances().add(newBalance);
                } catch (NoResultException e) {
                    throw new CurrencyNotFoundException("Currency not found: " + currencyUuid);
                }
            }
        }
    }

    @Override
    public void delete(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a LEFT JOIN FETCH a.wallet WHERE a.uuid = :uuid OR a.nickname=:name",
                                AccountDb.class)
                        .setParameter("uuid", account.getUuid().toString())
                        .setParameter("name", account.getNickname())
                        .getSingleResult();

                session.remove(accountDb);
                tx.commit();
            } catch (NoResultException e) {
                throw new AccountNotFoundException("Account no encontrado: " + account.getUuid());
            }catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(Account account) {
        save(account);
    }

    @Override
    public void create(Account account) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Long count = session.createQuery("SELECT COUNT(a) FROM AccountDb a WHERE a.uuid = :uuid", Long.class)
                        .setParameter("uuid", account.getUuid().toString())
                        .getSingleResult();

                if (count > 0) {
                    throw new AccountAlreadyExist("Account Ya existe: " + account.getUuid());
                }

                // Create new account
                AccountDb accountDb = new AccountDb();
                accountDb.setUuid(account.getUuid().toString());
                accountDb.setNickname(account.getNickname());
                accountDb.setCanReceiveCurrency(account.canReceiveCurrency());

                // Create new wallet
                WalletDb walletDb = new WalletDb();

                // First persist the wallet to get an ID
                session.persist(walletDb);

                // Now associate wallet with account
                accountDb.setWallet(walletDb);

                // Process balances
                for (Money domainMoney : account.getBalances()) {
                    String currencyUuid = domainMoney.getCurrency().getUuid().toString();

                    CurrencyDb currencyDb = session.createQuery(
                                    "FROM CurrencyDb WHERE uuid = :uuid", CurrencyDb.class)
                            .setParameter("uuid", currencyUuid)
                            .getSingleResult();

                    BalanceDb balanceDb = new BalanceDb();
                    balanceDb.setCurrency(currencyDb);
                    balanceDb.setAmount(domainMoney.getAmount());
                    balanceDb.setWallet(walletDb);
                    walletDb.getBalances().add(balanceDb);
                }

                // Save the account after the wallet has been persisted
                session.persist(accountDb);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Account> getAccountsTopByCurrency(String currencyName, int limit, int offset) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // HQL query to navigate through wallet to balances
                String hql = "SELECT DISTINCT a FROM AccountDb a " +
                        "JOIN FETCH a.wallet w " +
                        "JOIN FETCH w.balances b " +
                        "JOIN FETCH b.currency c " +
                        "WHERE c.singular = :currencyName OR c.plural = :currencyName " +
                        "ORDER BY b.amount DESC";

                List<AccountDb> accountDbs = session.createQuery(hql, AccountDb.class)
                        .setParameter("currencyName", currencyName)
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList();

                tx.commit();
                // Convert to domain entities
                return accountDbs.stream()
                        .map(AccountMapper::toDomain)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error retrieving accounts by currency", e);
            }
        }
    }
}