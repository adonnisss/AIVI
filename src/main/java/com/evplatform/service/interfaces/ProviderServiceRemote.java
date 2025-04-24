package com.evplatform.service.interfaces;

import com.evplatform.vao.Provider;
import jakarta.ejb.Remote;
import java.util.List;
import java.util.Optional;

@Remote
public interface ProviderServiceRemote {
    int addProvider(Provider provider) throws IllegalArgumentException;
    Provider getProviderById(int id);
    Optional<Provider> findProviderById(int id);
    List<Provider> getAllProviders();
    boolean updateProvider(Provider provider) throws IllegalArgumentException;
    boolean deleteProvider(int id) throws IllegalStateException;
}