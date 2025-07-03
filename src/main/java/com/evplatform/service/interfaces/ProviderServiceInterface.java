package com.evplatform.service.interfaces;

import com.evplatform.vao.Provider;
import jakarta.ejb.Local; // <- DODAJTE
import java.util.List;
import java.util.Optional;

@Local // <- DODAJTE
public interface ProviderServiceInterface {
    int addProvider(Provider provider) throws IllegalArgumentException;
    Provider getProviderById(int id);
    Optional<Provider> findProviderById(int id);
    List<Provider> getAllProviders();
    boolean updateProvider(Provider provider) throws IllegalArgumentException;
    boolean deleteProvider(int id) throws IllegalStateException;
}