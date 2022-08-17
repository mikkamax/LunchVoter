package com.mike.lunchvoter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "vote")
public class Vote {

    @EmbeddedId
    private VoteIdentity voteIdentity;

    @NotNull
    @Column(name = "date")
    private LocalDate date;

}
