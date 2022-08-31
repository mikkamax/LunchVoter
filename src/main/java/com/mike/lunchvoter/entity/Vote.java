package com.mike.lunchvoter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Table(name = "vote")
public class Vote {

    @EmbeddedId
    private VoteIdentity voteIdentity;

    @NotNull
    @Column(name = "restaurant_id")
    private Integer restaurantId;

}
