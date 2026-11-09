package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.repository.RecommendationRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Charge;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Recommendation;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteClient implements Command<Void> {
    private String clientNif;
    private ClientRepository clientRepo = Factories.repository.forClient();
    private RecommendationRepository recommendationRepo = Factories.repository.forRecommendation();
    private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

    public DeleteClient(String clientNif) {
        ArgumentChecks.isNotBlank(clientNif, "Client nif cannot be null or blank");
        this.clientNif = clientNif;
    }

    @Override
    public Void execute() throws BusinessException {
        checkClientExists(clientNif);
        checkClientHasNoVehicles(clientNif);
        checkClientHasNoPaidInvoices(clientNif);

        Client client = clientRepo.findByNif(new Nif(clientNif)).get();

        for (PaymentMean pm : client.getPaymentMeans()) {
            Associations.Holds.unlink(pm);
            paymentMeanRepo.remove(pm);
        }

        if (client.getRecommendation() != null) {
            Recommendation r = client.getRecommendation();
            Associations.Recommends.unlink(r);
            recommendationRepo.remove(r);
        }

        for (Recommendation recommendation : client.getRecommendations()) {
            Associations.Recommends.unlink(recommendation);
            recommendationRepo.remove(recommendation);
        }

        clientRepo.remove(client);

        return null;
    }

    private void checkClientExists(String clientNif) throws BusinessException {
        Nif nif = new Nif(clientNif);
        Optional<Client> client = clientRepo.findByNif(nif);
        BusinessChecks.exists(client, "Client does not exist");
    }

    private void checkClientHasNoVehicles(String clientNif) throws BusinessException {
        Nif nif = new Nif(clientNif);
        Client client = clientRepo.findByNif(nif).get();
        BusinessChecks.isTrue(client.getVehicles().isEmpty(), "Client has vehicles");
    }

    private void checkClientHasNoPaidInvoices(String clientNif) throws BusinessException {
        Nif nif = new Nif(clientNif);
        Client client = clientRepo.findByNif(nif).get();

        for (PaymentMean pm : client.getPaymentMeans()) {
            for (Charge charge : pm.getCharges()) {
                BusinessChecks.isFalse(charge.getInvoice().isSettled(), "Client has paid invoices");
            }
        }
    }

}
