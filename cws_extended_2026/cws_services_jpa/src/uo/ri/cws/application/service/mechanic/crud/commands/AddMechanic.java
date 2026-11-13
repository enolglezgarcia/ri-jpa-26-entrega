package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {
    private MechanicRepository repo = Factories.repository.forMechanic();
    private MechanicDto dto;

    public AddMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "MechanicDto cannot be null");
        ArgumentChecks.isNotBlank(dto.nif, "Mechanic nif cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.name, "Mechanic name cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.surname, "Mechanic surname cannot be null or blank");
        this.dto = dto;
    }

    @Override
    public MechanicDto execute() throws BusinessException {
        BusinessChecks.isTrue(Nif.isValid(dto.nif), "The mechanic nif is not valid");
        Nif nif = new Nif(dto.nif);
        BusinessChecks.isTrue(FullName.isValid(dto.name, dto.surname), "The mechanic fullname is not valid");
        FullName fullname = new FullName(dto.name, dto.surname);
        Optional<Mechanic> om = repo.findByNif(nif);
        BusinessChecks.doesNotExist(om, "The mechanic is already registered");
        Mechanic m = new Mechanic(nif, fullname);
        repo.add(m);
        return DtoAssembler.toDto(m);
    }

}
