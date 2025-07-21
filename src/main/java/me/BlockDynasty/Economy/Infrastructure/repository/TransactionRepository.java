package me.BlockDynasty.Economy.Infrastructure.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;

import me.BlockDynasty.Economy.Infrastructure.repository.Mappers.AccountMapper;
import me.BlockDynasty.Economy.Infrastructure.repository.Models.Hibernate.*;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.persistence.transaction.ITransactions;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionRepository  implements ITransactions {

    private final SessionFactory sessionFactory;
    public TransactionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Result<TransferResult> transfer(String fromUuid, String toUuid, Currency currency, BigDecimal amount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb fromDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "JOIN FETCH a.wallet w " +
                                        "JOIN FETCH w.balances b " +
                                        "JOIN FETCH b.currency c " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", fromUuid)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .uniqueResult();

                AccountDb toDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "JOIN FETCH a.wallet w " +
                                        "JOIN FETCH w.balances b " +
                                        "JOIN FETCH b.currency c " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", toUuid)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .uniqueResult();

                if (fromDb == null || toDb == null) {
                    tx.rollback();
                    return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
                }

                Account from = AccountMapper.toDomain(fromDb);
                Account to = AccountMapper.toDomain(toDb);

                // Lógica de negocio de la cuenta
                Result<Void> result = from.subtract(currency, amount);
                if (!result.isSuccess()) {
                    tx.rollback(); // Para liberar el bloqueo pesimista
                    return Result.failure(result.getErrorMessage(), result.getErrorCode());
                }
                to.add(currency, amount);

                // Guardar las cuentas actualizadas
                updateBalancesInDb(from, fromDb, session);
                updateBalancesInDb(to, toDb, session);

                tx.commit();
                return Result.success(new TransferResult(from, to));
            } catch (NoResultException e) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            } catch (Exception e) {
                tx.rollback();
                return Result.failure("Error en la transferencia: " + e.getMessage(), ErrorCode.UNKNOWN_ERROR);
            }
        }
    }

    // Helper method to update balances in the database
    private void updateBalancesInDb(Account account, AccountDb accountDb, Session session) {
        WalletDb walletDb = accountDb.getWallet();

        for (Balance balance : account.getBalances()) {
            String currencyUuid = balance.getCurrency().getUuid().toString();

            // Find the corresponding BalanceDb
            Optional<BalanceDb> existingBalance = walletDb.getBalances().stream()
                    .filter(b -> b.getCurrency().getUuid().equals(currencyUuid))
                    .findFirst();

            if (existingBalance.isPresent()) {
                // Update existing balance
                existingBalance.get().setAmount(balance.getAmount());
            } else {
                // Create new balance
                CurrencyDb currencyDb;
                try {
                    currencyDb = session.createQuery(
                                    "FROM CurrencyDb WHERE uuid = :uuid", CurrencyDb.class)
                            .setParameter("uuid", currencyUuid)
                            .getSingleResult();
                } catch (NoResultException e) {
                    throw new CurrencyNotFoundException("Currency not found: " + currencyUuid);
                }

                BalanceDb newBalance = new BalanceDb();
                newBalance.setCurrency(currencyDb);
                newBalance.setAmount(balance.getAmount());
                newBalance.setWallet(walletDb);
                walletDb.getBalances().add(newBalance);
            }
        }
    }

    @Override
    public Result<Account> withdraw(String accountUuid, Currency currency, BigDecimal amount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "JOIN FETCH a.wallet w " +
                                        "JOIN FETCH w.balances b " +
                                        "JOIN FETCH b.currency c " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", accountUuid)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .uniqueResult();

                if (accountDb == null) {
                    tx.rollback();
                    return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
                }

                Account account = AccountMapper.toDomain(accountDb);
                Result<Void> result = account.subtract(currency, amount);

                if (!result.isSuccess()) {
                    tx.rollback();
                    return Result.failure(result.getErrorMessage(), result.getErrorCode());
                }

                // Use the helper method for consistent balance updates
                updateBalancesInDb(account, accountDb, session);

                tx.commit();
                return Result.success(account);
            } catch (NoResultException e) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            } catch (Exception e) {
                tx.rollback();
                return Result.failure("Error en el retiro: " + e.getMessage(), ErrorCode.UNKNOWN_ERROR);
            }
        }
    }

    @Override
    public Result<Account> deposit(String accountUuid, Currency currency, BigDecimal amount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // Changed query to properly fetch account with its wallet and balances
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "JOIN FETCH a.wallet w " +
                                        "JOIN FETCH w.balances b " +
                                        "JOIN FETCH b.currency c " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", accountUuid)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .uniqueResult();

                if (accountDb == null) {
                    tx.rollback();
                    return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
                }

                Account account = AccountMapper.toDomain(accountDb);
                Result<Void> result = account.add(currency, amount);

                if (!result.isSuccess()) {
                    tx.rollback();
                    return Result.failure(result.getErrorMessage(), result.getErrorCode());
                }

                // Use the helper method for consistent balance updates
                updateBalancesInDb(account, accountDb, session);

                tx.commit();
                return Result.success(account);
            } catch (NoResultException e) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            } catch (Exception e) {
                tx.rollback();
                return Result.failure("Error en el depósito: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
            }
        }
    }
    @Override
    public Result<Account> exchange(String playerUUID, Currency fromCurrency, BigDecimal amountFrom, Currency toCurrency, BigDecimal amountTo) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb playerDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "JOIN FETCH a.wallet w " +
                                        "JOIN FETCH w.balances b " +
                                        "JOIN FETCH b.currency c " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", playerUUID)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .uniqueResult();

                if (playerDb == null) {
                    tx.rollback();
                    return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
                }

                Account player = AccountMapper.toDomain(playerDb);

                Result<Void> resultSubtract = player.subtract(fromCurrency, amountFrom);
                if (!resultSubtract.isSuccess()) {
                    tx.rollback();
                    return Result.failure(resultSubtract.getErrorMessage(), resultSubtract.getErrorCode());
                }

                Result<Void> resultAdd = player.add(toCurrency, amountTo);
                if (!resultAdd.isSuccess()) {
                    tx.rollback();
                    return Result.failure(resultAdd.getErrorMessage(), resultAdd.getErrorCode());
                }

                // Use the helper method for consistent balance updates
                updateBalancesInDb(player, playerDb, session);

                tx.commit();
                return Result.success(player);
            } catch (NoResultException e) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            } catch (Exception e) {
                tx.rollback();
                return Result.failure("Error en el intercambio: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
            }
        }
    }

    @Override
    public Result<TransferResult> trade(String fromUuid, String toUuid, Currency fromCurrency, Currency toCurrency, BigDecimal amountFrom, BigDecimal amountTo) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AccountDb fromDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "JOIN FETCH a.wallet w " +
                                        "JOIN FETCH w.balances b " +
                                        "JOIN FETCH b.currency c " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", fromUuid)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .uniqueResult();

                AccountDb toDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "JOIN FETCH a.wallet w " +
                                        "JOIN FETCH w.balances b " +
                                        "JOIN FETCH b.currency c " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", toUuid)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .uniqueResult();

                if (fromDb == null || toDb == null) {
                    tx.rollback();
                    return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
                }

                Account from = AccountMapper.toDomain(fromDb);
                Account to = AccountMapper.toDomain(toDb);

                Result<Void> resultSubtractFrom = from.subtract(fromCurrency, amountFrom);
                if (!resultSubtractFrom.isSuccess()) {
                    tx.rollback();
                    return Result.failure(resultSubtractFrom.getErrorMessage(), resultSubtractFrom.getErrorCode());
                }

                Result<Void> resultSubtractTo = to.subtract(toCurrency, amountTo);
                if (!resultSubtractTo.isSuccess()) {
                    tx.rollback();
                    return Result.failure(resultSubtractTo.getErrorMessage(), resultSubtractTo.getErrorCode());
                }

                Result<Void> resultAddFrom = from.add(toCurrency, amountTo);
                if (!resultAddFrom.isSuccess()) {
                    tx.rollback();
                    return Result.failure(resultAddFrom.getErrorMessage(), resultAddFrom.getErrorCode());
                }

                Result<Void> resultAddTo = to.add(fromCurrency, amountFrom);
                if (!resultAddTo.isSuccess()) {
                    tx.rollback();
                    return Result.failure(resultAddTo.getErrorMessage(), resultAddTo.getErrorCode());
                }

                // Use the helper method for consistent balance updates
                updateBalancesInDb(from, fromDb, session);
                updateBalancesInDb(to, toDb, session);

                tx.commit();
                return Result.success(new TransferResult(from, to));
            } catch (NoResultException e) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            } catch (Exception e) {
                tx.rollback();
                return Result.failure("Error en el intercambio: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
            }
        }
    }

    @Override
    public Result<Account> setBalance(String accountUuid, Currency currency, BigDecimal amount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // Modified query to properly fetch the account with its wallet and balances
                AccountDb accountDb = session.createQuery(
                                "SELECT a FROM AccountDb a " +
                                        "JOIN FETCH a.wallet w " +
                                        "JOIN FETCH w.balances b " +
                                        "JOIN FETCH b.currency c " +
                                        "WHERE a.uuid = :uuid",
                                AccountDb.class)
                        .setParameter("uuid", accountUuid)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .uniqueResult();

                if (accountDb == null) {
                    tx.rollback();
                    return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
                }

                Account account = AccountMapper.toDomain(accountDb);
                Result<Void> result = account.setBalance(currency, amount);

                if (!result.isSuccess()) {
                    tx.rollback();
                    return Result.failure(result.getErrorMessage(), result.getErrorCode());
                }

                // Use the helper method for consistent balance updates
                updateBalancesInDb(account, accountDb, session);

                tx.commit();
                return Result.success(account);
            } catch (NoResultException e) {
                tx.rollback();
                return Result.failure("Cuenta no encontrada", ErrorCode.ACCOUNT_NOT_FOUND);
            } catch (Exception e) {
                tx.rollback();
                return Result.failure("Error al establecer balance: " + e.getMessage(), ErrorCode.DATA_BASE_ERROR);
            }
        }
    }
}
