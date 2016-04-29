/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tumitfahrer.daos.impl;

import de.tumitfahrer.daos.IGenericDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class GenericDao<T> implements IGenericDao<T> {

    protected Class<T> clazz;
    @Autowired
    protected SessionFactory sessionFactory;

    public GenericDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T load(Integer id) {
        if (id == null) {
            return null;
        }
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    @Override
    public void save(T object) {
        sessionFactory.getCurrentSession().save(object);
    }

    @Override
    public void update(T object) {
        sessionFactory.getCurrentSession().update(object);
    }

    @Override
    public void delete(T object) {
        sessionFactory.getCurrentSession().delete(object);
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByCriterion(Criterion... criterion) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
        for (Criterion c : criterion) {
            criteria.add(c);
        }
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByCriterion(List<Criterion> criterion) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
        for (Criterion c : criterion) {
            criteria.add(c);
        }
        return criteria.list();
    }
}
