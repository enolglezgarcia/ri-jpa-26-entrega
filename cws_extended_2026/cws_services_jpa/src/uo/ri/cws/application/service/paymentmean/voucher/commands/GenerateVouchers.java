package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchers implements Command<int[]> {
    private ClientRepository clientRepo = Factories.repository.forClient();
    private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();
    private int[] vouchersGenerated = new int[3];

    @Override
    public int[] execute() throws BusinessException {
        int vouchers20Created = 0;
        List<Client> clients = clientRepo.findAll();

        for (Client client : clients) {
            List<WorkOrder> validWorkOrders = client.getWorkOrdersAvailableForVoucher20();
            int vouchersToGenerate = validWorkOrders.size() / 3;

            for (int i = 0; i < vouchersToGenerate; i++) {
                Voucher v = new Voucher(VoucherCode.generate(), "Por tres órdenes de trabajo", Money.of("20.0"));
                Associations.Holds.link(client, v);
                paymentMeanRepo.add(v);
                vouchers20Created++;
            }

            for (int i = 0; i < vouchersToGenerate * 3; i++) {
                validWorkOrders.get(i).markAsUsedForVoucher();
            }
        }

        vouchersGenerated[0] = vouchers20Created;

        return vouchersGenerated;
    }

}
