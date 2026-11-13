package uo.ri.cws.application.service.client.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class FindClientsRecommendedBy implements Command<List<ClientDto>> {
    private String recommenderNif;
    private ClientRepository clientRepo = Factories.repository.forClient();

    public FindClientsRecommendedBy(String recommenderNif) {
        ArgumentChecks.isNotBlank(recommenderNif, "Recommender nif cannot be null or blank");
        this.recommenderNif = recommenderNif;
    }

    @Override
    public List<ClientDto> execute() throws BusinessException {
        BusinessChecks.isTrue(Nif.isValid(recommenderNif));
        return DtoAssembler.toDtoList(clientRepo.findRecommendedBy(new Nif(recommenderNif)));
    }

}
