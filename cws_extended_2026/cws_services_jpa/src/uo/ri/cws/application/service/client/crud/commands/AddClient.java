package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.repository.RecommendationRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Recommendation;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddClient implements Command<ClientDto> {

    private ClientDto dto;
    private String recommenderNif;
    private ClientRepository clientRepo = Factories.repository.forClient();
    private RecommendationRepository recommendationRepo = Factories.repository.forRecommendation();
    private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

    public AddClient(ClientDto dto, String recommenderNif) {
        ArgumentChecks.isNotNull(dto, "Client dto cannot be null");
        ArgumentChecks.isNotBlank(dto.nif, "Client nif cannot be null or empty");
        ArgumentChecks.isNotBlank(dto.name, "Client name cannot be null or empty");
        ArgumentChecks.isNotBlank(dto.surname, "Client surname cannot be null or empty");
        ArgumentChecks.isNotBlank(dto.addressCity, "Client ciry cannot be null or empty");
        ArgumentChecks.isNotBlank(dto.addressStreet, "Client street cannot be null or empty");
        ArgumentChecks.isNotBlank(dto.addressCountry, "Client country cannot be null or empty");
        ArgumentChecks.isNotBlank(dto.addressZipcode, "Client zipcode cannot be null or empty");
        ArgumentChecks.isNotBlank(dto.phone, "Client phone cannot be null or empty");
        ArgumentChecks.isNotBlank(dto.email, "Client email cannot be null or empty");

        if (recommenderNif != null) {
            ArgumentChecks.isNotBlank(recommenderNif, "Recommender nif cannot be empty");
        }

        this.dto = dto;
        this.recommenderNif = recommenderNif;
    }

    @Override
    public ClientDto execute() throws BusinessException {
        Nif clientNif = new Nif(dto.nif);
        FullName fullname = new FullName(dto.name, dto.surname);
        Address address = new Address(dto.addressStreet, dto.addressCity, dto.addressCountry, dto.addressZipcode);
        PhoneNumber phone = new PhoneNumber(dto.phone);
        Email email = new Email(dto.email);

        Optional<Client> oc = clientRepo.findByNif(clientNif);
        BusinessChecks.doesNotExist(oc, "The client is already registered");
        Client client = new Client(clientNif, fullname, email, phone, address);
        clientRepo.add(client);

        if (recommenderNif != null) {
            Nif recoNif = new Nif(recommenderNif);
            Optional<Client> recommender = clientRepo.findByNif(recoNif);
            BusinessChecks.exists(recommender, "The recommender does not exist");
            BusinessChecks.isTrue(!clientNif.equals(recoNif), "A client cannot be recommend by itself");
            Recommendation recommendation = new Recommendation(recommender.get(), client);
            recommendationRepo.add(recommendation);
        }

        Cash cash = new Cash(client);
        paymentMeanRepo.add(cash);

        return DtoAssembler.toDto(client);
    }

}
