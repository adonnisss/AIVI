package com.evplatform.dao;

import com.evplatform.dao.interfaces.ProviderDAOInterface;
import com.evplatform.vao.Provider;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class ProviderDAO implements ProviderDAOInterface {

    private final List<Provider> providers = Collections.synchronizedList(new ArrayList<>());
    private int nextId = 1;

    public ProviderDAO() {
    }

    @Override
    public int add(Provider provider) {
        synchronized (providers) {
            provider.setId(nextId++);
            providers.add(provider);
            return provider.getId();
        }
    }

    @Override
    public Provider getById(int id) {
        return findById(id).orElse(null);
    }

    @Override
    public List<Provider> getAll() {
        synchronized (providers) {
            return new ArrayList<>(providers);
        }
    }

    @Override
    public boolean update(Provider provider) {
        synchronized (providers) {
            Optional<Provider> existingProvider = findById(provider.getId());

            if (existingProvider.isPresent()) {
                int index = providers.indexOf(existingProvider.get());
                providers.set(index, provider);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        synchronized (providers) {
            return providers.removeIf(provider -> provider.getId() == id);
        }
    }

    @Override
    public Optional<Provider> findById(int id) {
        synchronized (providers) {
            return providers.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst();
        }
    }
}