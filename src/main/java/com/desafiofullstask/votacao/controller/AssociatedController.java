package com.desafiofullstask.votacao.controller;

import com.desafiofullstask.votacao.entity.Associated;
import com.desafiofullstask.votacao.service.AssociatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/associated")
public class AssociatedController {

    @Autowired
    private AssociatedService associatedService;


    @PostMapping
    public ResponseEntity save(
            @RequestBody Associated associated
    ) {
        return ResponseEntity.ok().body(associatedService.save(associated));
    }

    @PutMapping
    public ResponseEntity update(
            @RequestBody Associated associated
    ) {
        return ResponseEntity.ok().body(associatedService.save(associated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Integer id
    ) {
        associatedService.delete(id);
        return ResponseEntity.ok().build();
    }

}
