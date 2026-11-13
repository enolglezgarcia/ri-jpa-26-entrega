package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.voucher.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class FindVouchersByClientNif implements Command<List<VoucherDto>> {
    private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();
    private String clientNif;

    public FindVouchersByClientNif(String clientNif) {
        ArgumentChecks.isNotBlank(clientNif, "Client nif cannot be null or empty");
        this.clientNif = clientNif;
    }

    @Override
    public List<VoucherDto> execute() throws BusinessException {
        BusinessChecks.isTrue(Nif.isValid(clientNif), "The client nif is not valid");
        return DtoAssembler.toDtoList(paymentMeanRepo.findVouchersByClientNif(new Nif(clientNif)));
    }

}
