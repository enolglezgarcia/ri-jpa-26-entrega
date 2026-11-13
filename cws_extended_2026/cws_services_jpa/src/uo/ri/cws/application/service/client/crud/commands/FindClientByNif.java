package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class FindClientByNif implements Command<Optional<ClientDto>> {
    private String nif;
    private ClientRepository clientRepo = Factories.repository.forClient();

    public FindClientByNif(String nif) {
        ArgumentChecks.isNotBlank(nif, "Nif cannot be null or blank");
        this.nif = nif;
    }

    @Override
    public Optional<ClientDto> execute() throws BusinessException {
        BusinessChecks.isTrue(Nif.isValid(nif), "The client nif is not valid");
        return clientRepo.findByNif(new Nif(nif)).map(c -> DtoAssembler.toDto(c));
    }

}
