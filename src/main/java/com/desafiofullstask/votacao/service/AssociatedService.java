package com.desafiofullstask.votacao.service;

import com.desafiofullstask.votacao.entity.Associated;
import com.desafiofullstask.votacao.repository.AssociatedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociatedService {

    private static final Logger logger = LoggerFactory.getLogger(AssociatedService.class);

    @Autowired
    private AssociatedRepository associatedRepository;

    /**
     * Método responsável por salvar um associado e verificar se já não existe, caso sim, ele não salva, para não ter duplicidade.
     */
    public String save(Associated associated) {
        try {
            if(associatedRepository.findByCpf(associated.getCpf()) == null) {
                associatedRepository.save(associated);
                return "ASSOCIATED_REGISTERED_SUCCESSFULLY";
            }else{
                logger.info("Associado já cadastrado");
                return "ASSOCIATED_ALREADY_REGISTERED";
            }

        } catch (Exception e) {
            logger.error("Error saving associated: {}", e.getMessage());
            return null;
        }
    }

    public void delete(Integer id) {
        try {
            associatedRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting associated: {}", e.getMessage());
        }

    }
}
