package uo.ri.cws.application.service.client.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.util.exception.BusinessException;

public class ClientCrudServiceImpl implements ClientCrudService {

    @Override
    public ClientDto addClient(ClientDto client, String recommenderNif) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteClientByNif(String nif) throws BusinessException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateClient(ClientDto client) throws BusinessException {
        // TODO Auto-generated method stub

    }

    @Override
    public List<ClientDto> findAllClients() throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<ClientDto> findClientByNif(String nif) throws BusinessException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<ClientDto> findClientsRecommendedBy(String recommenderNif) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

}
