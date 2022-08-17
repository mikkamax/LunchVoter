package com.mike.lunchvoter.repository;

import com.mike.lunchvoter.entity.Vote;
import com.mike.lunchvoter.entity.VoteIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteIdentity> {
}
