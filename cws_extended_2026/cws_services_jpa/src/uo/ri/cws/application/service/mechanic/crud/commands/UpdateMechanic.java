package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {

    private MechanicDto dto;
    private MechanicRepository repo = Factories.repository.forMechanic();

    public UpdateMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "The mechanic dto is null");
        ArgumentChecks.isNotBlank(dto.nif, "The mechanic nif is null or empty");
        ArgumentChecks.isNotBlank(dto.name, "The mechanic name is null or empty");
        ArgumentChecks.isNotBlank(dto.surname, "The mechanic surname is null or empty");

        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Mechanic> om = repo.findById(dto.id);
        BusinessChecks.exists(om, "The mechanic does not exist");
        Mechanic m = om.get();
        BusinessChecks.hasVersion(dto.version, m.getVersion(), "Data out of date");
        BusinessChecks.isTrue(Nif.isValid(dto.nif), "The mechanic nif is not valid");
        Nif nif = new Nif(dto.nif);

        if (!nif.equals(m.getNif())) {
            checkNifDoesNotExist(nif);
            m.setNif(nif);
        }
        BusinessChecks.isTrue(FullName.isValid(dto.name, dto.surname), "The mechanic fullname is not valid");
        FullName fullname = new FullName(dto.name, dto.surname);
        m.setFullName(fullname);
        m.updatedNow();
        return null;
    }

    private void checkNifDoesNotExist(Nif nif) throws BusinessException {
        Optional<Mechanic> om = repo.findByNif(nif);
        BusinessChecks.doesNotExist(om, "The mechanic is already registered");
    }

}
