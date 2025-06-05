package br.dev.ederson.spring.cooperativa.controller;


import br.dev.ederson.spring.cooperativa.exception.BadRequestException;
import br.dev.ederson.spring.cooperativa.exception.NotFoundException;
import br.dev.ederson.spring.cooperativa.model.*;
import br.dev.ederson.spring.cooperativa.service.AgendaService;
import br.dev.ederson.spring.cooperativa.service.AssociateService;
import br.dev.ederson.spring.cooperativa.service.SessionService;
import br.dev.ederson.spring.cooperativa.service.VoteService;
import br.dev.ederson.spring.cooperativa.to.VoteTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AssemblyRestController {

    @Autowired private AgendaService agendaService;
    @Autowired private AssociateService associateService;
    @Autowired private SessionService sessionService;
    @Autowired private VoteService voteService;

    @GetMapping("/agenda")
    public List<Agenda> getAgendas() {
        return agendaService.getAllAgendas();
    }

    @GetMapping("/agenda/session")
    public List<Agenda> getAgendasBySession(@Param("sessionId") Long sessionId) throws BadRequestException {
        Session session = sessionService.getById(sessionId);
        if (session == null)
            throw new BadRequestException("Session " + sessionId + " not found!");

        return agendaService.getAgendasBySession(session);
    }

    @GetMapping("/agenda/associate")
    public List<Agenda> getAgendasByAssociate(@Param("associateId") Long associateId) throws BadRequestException {
        Associate associate = associateService.getById(associateId);
        if (associate == null)
            throw new BadRequestException("Associate " + associateId + " not found!");

        return voteService.getAgendasByAssociate(associate);
    }

    @PostMapping("/associate")
    public Associate createAssociate(@RequestBody Associate associate) throws BadRequestException {
        return associateService.createAssociate(associate);
    }

    @GetMapping("/getAssociateByDocument")
    public Associate getAssociate(@Param("document") String document) throws BadRequestException {
        Associate associate = associateService.getAssociateByDocument(document);

        if (associate == null)
            throw new BadRequestException("Associate with document " + document + " not found!");

        return associate;
    }

    @PostMapping("/agenda")
    public Response createAgenda(@RequestBody String description) throws BadRequestException {
        Agenda agenda = agendaService.createAgenda(description);
        return new Response("Agenda " + agenda.getId() + " registered successfully!");
    }

    @GetMapping("/session")
    public List<Session> getSessions() {
        return sessionService.getSessions();
    }

    @GetMapping("/sessionNotInitialized")
    public List<Session> getAllSessionsNotInitialized() {
        return sessionService.getAllSessionsNotInitialized();
    }

    @PostMapping("/session")
        public Response createSession(@Param("durationTime") int durationTime) {
        Session session = new Session();
        session.setDurationTime(durationTime);

        session = sessionService.createSession(session);
        return new Response("Session " + session.getId() + " registered successfully!");
    }

    @PostMapping("/addAgendaToSession")
    public Response addAgendaToSession(@Param("agendaId") Long agendaId, @Param("sessionId") Long sessionId) throws BadRequestException, NotFoundException {
        Agenda agenda = agendaService.getById(agendaId);
        if (agenda == null)
            throw new NotFoundException("Agenda " + agendaId + " not found!");

        Session session = sessionService.getById(sessionId);
        if (session == null)
            throw new NotFoundException("Session " + sessionId + " not found!");

        agendaService.addAgendaToSession(agenda, session);
        return new Response("Agenda has added to Session successfully!");
    }

    @PostMapping("/startSession")
    public Response startSession(@Param("sessionId") Long sessionId) throws BadRequestException, NotFoundException {
        Session session = sessionService.startSession(sessionId);
        return new Response("Session " + session.getId() + " started and will last for " + session.getDurationTime() + " minutes.");
    }

    @PostMapping("/vote")
    public Response vote(@RequestBody VoteTO voteTO) throws BadRequestException, NotFoundException {
        Associate associate = associateService.getById(voteTO.associateId());
        if (associate == null)
            throw new NotFoundException("Associate " + voteTO.associateId() + " not found!");

        Agenda agenda = agendaService.getById(voteTO.agendaId());
        if (agenda == null)
            throw new NotFoundException("Agenda " + voteTO.agendaId() + " not found!");

        Session session = agenda.getSession();
        if (session == null || !sessionService.isSessionOpenToVote(session))
            throw new NotFoundException("This Agenda is not abble to vote yet.");

        if (voteTO.approve() == null)
            throw new BadRequestException("You need to specify your Vote on this Agenda.");

        voteService.registerVote(associate, agenda, voteTO.approve());
        return new Response("Vote registered successfully!");
    }

    @PostMapping("/voteStatus")
    public Boolean getVoteStatus(@RequestBody VoteTO voteTO) {
        Vote vote = voteService.getVoteStatus(voteTO.agendaId(), voteTO.associateId());
        if (vote == null)
            return null;

        return vote.isApprove();
    }
}
