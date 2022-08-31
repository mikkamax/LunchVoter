package com.mike.lunchvoter.service;

import com.mike.lunchvoter.dto.RestaurantDto;
import com.mike.lunchvoter.dto.VoteDto;
import com.mike.lunchvoter.entity.Restaurant;
import com.mike.lunchvoter.entity.Vote;
import com.mike.lunchvoter.entity.VoteIdentity;
import com.mike.lunchvoter.exception.AlreadyVotedTodayException;
import com.mike.lunchvoter.exception.CustomConstraintViolationException;
import com.mike.lunchvoter.exception.ObjectNotFoundException;
import com.mike.lunchvoter.mapping.RestaurantMapper;
import com.mike.lunchvoter.mapping.VoteMapper;
import com.mike.lunchvoter.repository.RestaurantRepository;
import com.mike.lunchvoter.repository.VoteRepository;
import com.mike.lunchvoter.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private static final ExampleMatcher EXAMPLE_MATCHER_FOR_GET_BY_PARAMS = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final VoteRepository voteRepository;
    private final VoteMapper voteMapper;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository,
                             RestaurantMapper restaurantMapper,
                             VoteRepository voteRepository,
                             VoteMapper voteMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
    }

    @Transactional
    public RestaurantDto create(RestaurantDto restaurantDto) {
        return createOrUpdate(restaurantDto);
    }

    public RestaurantDto get(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurantMapper::mapToDtoWithoutMenus)
                .orElseThrow(() -> objectNotFoundException(restaurantId));
    }

    public List<RestaurantDto> getAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::mapToDtoWithoutMenus)
                .toList();
    }

    public List<RestaurantDto> getByParams(String name, String address) {
        Example<Restaurant> example = Example.of(
                new Restaurant()
                        .setName(name)
                        .setAddress(address),
                EXAMPLE_MATCHER_FOR_GET_BY_PARAMS
        );

        return restaurantRepository.findAll(example)
                .stream()
                .map(restaurantMapper::mapToDtoWithoutMenus)
                .toList();
    }

    public List<RestaurantDto> getAllWithMenusForToday() {
        return restaurantRepository.findByMenus_Date(LocalDate.now())
                .stream()
                .map(restaurantMapper::mapToDtoWithMenus)
                .toList();
    }

    @Transactional
    public VoteDto voteForRestaurant(Integer restaurantId, Integer userId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        VoteIdentity voteIdentity = new VoteIdentity(userId, localDateTime.toLocalDate());

        throwIfUserAlreadyVotedTodayAndItsTooLateToChangeVote(localDateTime.toLocalTime(), voteIdentity);

        Vote vote = new Vote(voteIdentity, restaurantId);

        return voteMapper.mapToDto(
                voteRepository.save(vote)
        );
    }

    @Transactional
    public RestaurantDto update(Integer restaurantId, RestaurantDto restaurantDto) {
        checkIfRestaurantWithThisIdExistsOrElseThrow(restaurantId);
        return createOrUpdate(restaurantDto);
    }

    @Transactional
    public void delete(Integer restaurantId) {
        checkIfRestaurantWithThisIdExistsOrElseThrow(restaurantId);
        restaurantRepository.deleteById(restaurantId);
    }

    private RestaurantDto createOrUpdate(RestaurantDto restaurantDto) {
        checkForNoConstraintViolationsOrElseThrow(restaurantDto);

        Restaurant restaurant = restaurantMapper.mapToEntityWithoutMenus(restaurantDto);

        return restaurantMapper.mapToDtoWithoutMenus(
                restaurantRepository.save(restaurant)
        );
    }

    private void checkIfRestaurantWithThisIdExistsOrElseThrow(Integer restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw objectNotFoundException(restaurantId);
        }
    }

    private void checkForNoConstraintViolationsOrElseThrow(RestaurantDto restaurantDto) {
        boolean isConstraintViolated = restaurantRepository.existsByIdNotAndNameEqualsIgnoreCaseAndAddressEqualsIgnoreCase(
                Optional.ofNullable(restaurantDto.getId()).orElse(Constants.NON_EXISTING_ID),
                restaurantDto.getName(),
                restaurantDto.getAddress()
        );

        if (isConstraintViolated) {
            throw new CustomConstraintViolationException("Cannot save " + restaurantDto
                    + " because Restaurant with same name and address already exists in the database");
        }
    }

    private void throwIfUserAlreadyVotedTodayAndItsTooLateToChangeVote(LocalTime localTime,
                                                                       VoteIdentity voteIdentity) {
        if (localTime.isAfter(Constants.TIME_AFTER_WHICH_USER_CANT_CHANGE_VOTE_FOR_TODAY)) {
            if (voteRepository.existsById(voteIdentity)) {
                throw new AlreadyVotedTodayException("User with id = " + voteIdentity.getUserId()
                        + " already voted today");
            }
        }
    }

    private static ObjectNotFoundException objectNotFoundException(Integer restaurantId) {
        return new ObjectNotFoundException("There is no restaurant with id = " + restaurantId);
    }

}
