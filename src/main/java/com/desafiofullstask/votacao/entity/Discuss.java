package com.desafiofullstask.votacao.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "dp_discuss")
public class Discuss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int totalVotesYes;

    @Column
    private int totalVotesNo;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session", nullable = true)
    private Session session;

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public int getTotalVotesYes() {
        return totalVotesYes;
    }

    public void setTotalVotesYes(int totalVotesYes) {
        this.totalVotesYes = totalVotesYes;
    }

    public int getTotalVotesNo() {
        return totalVotesNo;
    }

    public void setTotalVotesNo(int totalVotesNo) {
        this.totalVotesNo = totalVotesNo;
    }

}
