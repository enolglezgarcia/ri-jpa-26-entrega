package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateClient implements Command<Void> {
    private ClientDto dto;
    private ClientRepository clientRepo = Factories.repository.forClient();

    public UpdateClient(ClientDto dto) {
        ArgumentChecks.isNotNull(dto, "The client dto is null");
        ArgumentChecks.isNotBlank(dto.id, "Client id cannot be null or empty");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Client> oc = clientRepo.findById(dto.id);
        BusinessChecks.exists(oc, "The client does not exist");
        Client c = oc.get();
        BusinessChecks.hasVersion(dto.version, c.getVersion(), "Data out of date");
        Nif nif = new Nif(dto.nif);
        if (!nif.equals(c.getNif())) {
            checkNifDoesNotExist(nif);
            c.setNif(nif);
        }
        FullName fullname = new FullName(dto.name, dto.surname);
        c.setFullName(fullname);
        PhoneNumber phone = new PhoneNumber(dto.phone);
        c.setPhone(phone);
        Address address = new Address(dto.addressStreet, dto.addressCity, dto.addressCountry, dto.addressZipcode);
        c.setAddress(address);
        Email email = new Email(dto.email);
        c.setEmail(email);
        c.updatedNow();
        return null;
    }

    private void checkNifDoesNotExist(Nif nif) throws BusinessException {
        Optional<Client> oc = clientRepo.findByNif(nif);
        BusinessChecks.doesNotExist(oc, "The client is already registered");
    }

}
