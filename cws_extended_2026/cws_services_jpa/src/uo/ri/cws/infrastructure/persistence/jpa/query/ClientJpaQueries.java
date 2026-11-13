package uo.ri.cws.infrastructure.persistence.jpa.query;

import uo.ri.cws.application.query.ClientQueries;

public class ClientJpaQueries implements ClientQueries {

    @Override
    public boolean hasPaidInvoices(String clientId) {
        return false;
    }

}
