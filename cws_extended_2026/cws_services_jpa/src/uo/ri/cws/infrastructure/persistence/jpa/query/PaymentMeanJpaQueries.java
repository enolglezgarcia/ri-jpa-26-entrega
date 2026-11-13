package uo.ri.cws.infrastructure.persistence.jpa.query;

import java.util.List;

import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;

public class PaymentMeanJpaQueries implements PaymentMeanQueries {

    @Override
    public boolean hasCharges(String paymentMeanId) {
        return false;
    }

    @Override
    public List<VoucherSummaryDto> getVouchersSummary() {
        return null;
    }

}
