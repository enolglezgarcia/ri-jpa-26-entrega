package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrders implements Command<List<InvoicingWorkOrderDto>> {
    String nif;
    private WorkOrderRepository repo = Factories.repository.forWorkOrder();

    public FindNotInvoicedWorkOrders(String nif) {
        ArgumentChecks.isNotBlank(nif, "Client nif cannot be null or empty");
        this.nif = nif;
    }

    @Override
    public List<InvoicingWorkOrderDto> execute() throws BusinessException {
        BusinessChecks.isTrue(Nif.isValid(nif), "The client nif is not valid");
        return DtoAssembler.toInvoicingWorkOrderDtoList(repo.findNotInvoicedByClientNif(new Nif(nif)));
    }

}
