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

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyAlreadyExist;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.persistence.Exceptions.RepositoryException;
import BlockDynasty.Economy.domain.persistence.entities.ICurrencyRepository;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.Mappers.CurrencyMapper;
import repository.Models.CurrencyDb;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyRepository implements ICurrencyRepository {
    private SessionFactory sessionFactory;

    public CurrencyRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ICurrency> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                List<ICurrency> currencies = session.createQuery("SELECT c FROM CurrencyDb c", CurrencyDb.class)
                        .getResultList()
                        .stream()
                        .map(CurrencyMapper::toDomain)
                        .collect(Collectors.toList());
                tx.commit();
                return currencies;
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public ICurrency findByUuid(String uuid) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb entity = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.uuid = :uuid", CurrencyDb.class)
                        .setParameter("uuid", uuid)
                        .getSingleResult();
                tx.commit();
                return CurrencyMapper.toDomain(entity);
            } catch (NoResultException e) {
                tx.rollback();
                throw new CurrencyNotFoundException("Currency no encontrado: " + uuid);
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(), e);
            }
        }
    }

    @Override
    public ICurrency findDefaultCurrency() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb entity = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.defaultCurrency = :isDefault", CurrencyDb.class)
                        .setParameter("isDefault", true)
                        .getSingleResult();
                tx.commit();
                return CurrencyMapper.toDomain(entity);
            } catch (NoResultException e) {
                tx.rollback();
                throw new CurrencyNotFoundException("Currency default no encontrado: ");
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(), e);
            }
        }
    }

    @Override
    public ICurrency findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb entity = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.singular = :name OR c.plural = :name", CurrencyDb.class)
                        .setParameter("name", name)
                        .getSingleResult();
                tx.commit();
                return CurrencyMapper.toDomain(entity);
            } catch (NoResultException e) {
                tx.rollback();
                throw new CurrencyNotFoundException(e.getMessage());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void save(ICurrency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb currencyDb = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.uuid = :uuid OR c.plural = :plural OR c.singular = :singular", CurrencyDb.class)
                        .setParameter("uuid", currency.getUuid().toString())
                        .setParameter("plural", currency.getPlural())
                        .setParameter("singular", currency.getSingular())
                        .getSingleResult();

                CurrencyMapper.update(currency,currencyDb);
                session.merge(currencyDb);
                tx.commit();
            } catch (NoResultException e) {
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void delete(ICurrency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb currencyDb = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.uuid = :uuid OR c.plural = :plural OR c.singular = :singular", CurrencyDb.class)
                        .setParameter("uuid", currency.getUuid().toString())
                        .setParameter("plural", currency.getPlural())
                        .setParameter("singular", currency.getSingular())
                        .getSingleResult();

                session.remove(currencyDb);
                tx.commit();
            }catch (NoResultException e) {
                tx.rollback();
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void update(ICurrency currency) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CurrencyDb currencyDb = session.createQuery(
                                "SELECT c FROM CurrencyDb c WHERE c.uuid = :uuid OR c.plural = :plural OR c.singular = :singular", CurrencyDb.class)
                        .setParameter("uuid", currency.getUuid().toString())
                        .setParameter("plural", currency.getPlural())
                        .setParameter("singular", currency.getSingular())
                        .getSingleResult();

                CurrencyMapper.update(currency,currencyDb);
                session.merge(currencyDb);
                tx.commit();
            } catch (NoResultException e) {
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            } catch (Exception e) {
                tx.rollback();
                throw new  RepositoryException("Error repositorio: "+e.getMessage(),e);
            }
        }
    }

    @Override
    public void create(ICurrency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency no puede ser nulo");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // Check for existing currency with same UUID, singular or plural name
                Long count = session.createQuery(
                                "SELECT COUNT(c) FROM CurrencyDb c WHERE c.uuid = :uuid OR c.singular = :singular OR c.plural = :plural", Long.class)
                        .setParameter("uuid", currency.getUuid().toString())
                        .setParameter("singular", currency.getSingular())
                        .setParameter("plural", currency.getPlural())
                        .getSingleResult();

                if (count > 0) {
                    throw new CurrencyAlreadyExist("Ya existe una moneda con el mismo UUID, nombre singular o plural");
                }

                CurrencyDb entity = CurrencyMapper.toEntity(currency);
                session.persist(entity);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RepositoryException("Error al crear la moneda: " + e.getMessage(), e);
            }
        }
    }
}
