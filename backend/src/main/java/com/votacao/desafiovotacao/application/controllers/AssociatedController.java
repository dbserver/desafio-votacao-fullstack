package com.votacao.desafiovotacao.application.controllers;

import com.votacao.desafiovotacao.application.dtos.AssociatedDTO;
import com.votacao.desafiovotacao.domain.exceptions.CPFAlreadyExistsException;
import com.votacao.desafiovotacao.domain.exceptions.CPFInvalidException;
import com.votacao.desafiovotacao.domain.exceptions.NameNeededException;
import com.votacao.desafiovotacao.domain.services.AssociatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/associated")
public class AssociatedController {

    @Autowired
    private AssociatedService associatedService;

    @PostMapping("/create")
    @CrossOrigin("*")
    public ResponseEntity<?> createAssociated(@RequestBody AssociatedDTO associatedDTO)
            throws NameNeededException, CPFInvalidException, CPFAlreadyExistsException {
        return new ResponseEntity<>(associatedService.createAssociated(associatedDTO), HttpStatus.CREATED);
    }

    @GetMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> get(@RequestParam(value = "id") String id) {
        return new ResponseEntity<>(associatedService.get(id), HttpStatus.OK);
    }

    @GetMapping
    @CrossOrigin("*")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(associatedService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> update(@RequestParam(value = "id") String id, @RequestBody AssociatedDTO associatedDTO) {
        return new ResponseEntity<>(associatedService.update(id, associatedDTO), HttpStatus.OK);
    }

    @DeleteMapping("/id")
    @CrossOrigin("*")
    public ResponseEntity<?> delete(@RequestParam(value = "id") String id) {
        associatedService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @CrossOrigin("*")
    public ResponseEntity<?> deleteAll() {
        associatedService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
