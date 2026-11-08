package uo.ri.cws.application.service.paymentmean.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.exception.BusinessException;

public class PaymentMeanCrudServiceImpl implements PaymentMeanCrudService {

    @Override
    public CardDto addCard(CardDto card) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VoucherDto addVoucher(VoucherDto voucher) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deletePaymentMean(String id) throws BusinessException {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<PaymentMeanDto> findById(String id) throws BusinessException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<PaymentMeanDto> findPaymentMeansByClientNif(String nif) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<VoucherDto> findVoucherByCode(String code) throws BusinessException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Optional<CardDto> findCreditCardByNumber(String cardNumber) throws BusinessException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

}
