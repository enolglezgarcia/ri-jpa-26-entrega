package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class ClientJpaRepository extends BaseJpaRepository<Client> implements ClientRepository {

    @Override
    public Optional<Client> findByNif(Nif nif) {
        return Jpa.getManager().createNamedQuery("Client.findByNif", Client.class).setParameter(1, nif)
            .getResultStream().findFirst();

    }

    @Override
    public List<Client> findRecommendedBy(Nif recommenderNif) {
        return Jpa.getManager().createNamedQuery("Client.findByRecommenderNif", Client.class)
            .setParameter(1, recommenderNif).getResultList();
    }

    @Override
    public List<Client> findWithWorkOrdersUnusedForVoucher(int minimumWorkOrders) {
        return null;
    }

    @Override
    public List<Client> findWithRecommendationsUnusedForVoucher(int minimumRecommendations) {
        return null;
    }

}
