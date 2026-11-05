package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.query.InvoiceQueries;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto> {

    private List<String> workOrderIds;
    private WorkOrderRepository workOrderRepo = Factories.repository.forWorkOrder();
    private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();
    private InvoiceQueries queries = Factories.query.forInvoice();

    public CreateInvoiceFor(List<String> workOrderIds) {
        ArgumentChecks.isNotNull(workOrderIds);
        ArgumentChecks.isFalse(workOrderIds.isEmpty());
        ArgumentChecks.isFalse(workOrderIds.stream().anyMatch(i -> i == null));

        this.workOrderIds = workOrderIds;
    }

    @Override
    public InvoiceDto execute() throws BusinessException {
        List<WorkOrder> workOrders = workOrderRepo.findByIds(workOrderIds);
        BusinessChecks.isTrue(allWorkOrdersExist(workOrders), "All WorkOrders must exist");
        BusinessChecks.isTrue(allWorkOrdersAreApproved(workOrders), "WorkOrders must be in APPROVED state");
        Long invoiceNumber = queries.getNextInvoiceNumber();
        Invoice invoice = new Invoice(invoiceNumber, workOrders);
        invoiceRepo.add(invoice);
        return DtoAssembler.toDto(invoice);
    }

    private boolean allWorkOrdersAreApproved(List<WorkOrder> workOrders) {
        return workOrders.stream().allMatch(wo -> wo.isApproved());
    }

    private boolean allWorkOrdersExist(List<WorkOrder> workOrders) {
        return workOrders.size() == workOrderIds.size();
    }

}
