package com.enigma.x_food.feature.friend;

import com.enigma.x_food.feature.friend.dto.request.FriendRequest;
import com.enigma.x_food.feature.friend.dto.request.SearchFriendRequest;
import com.enigma.x_food.feature.friend.dto.response.FriendResponse;
import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserService userService;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FriendResponse createNew(FriendRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);

        User user1 = userService.getUserById(request.getAccountID1());
        User user2 = userService.getUserById(request.getAccountID2());

        Friend friend = Friend.builder()
                .user1(user1)
                .user2(user2)
                .build();

        friendRepository.saveAndFlush(friend);

        return mapToResponse(friend);

    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendResponse> findByAccountId(SearchFriendRequest request) {
        Specification<Friend> specification = getAllFriendSpecification(request);
        return friendRepository.findAll(specification).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Friend> findByFriendId(SearchFriendRequest request) {
        Specification<Friend> specification = getFriendSpecification(request);
        return friendRepository.findAll(specification);
    }

    private FriendResponse mapToResponse(Friend friend) {
        return FriendResponse.builder()
                .friendID(friend.getFriendID())
                .accountID1(friend.getUser1().getAccountID())
                .accountID2(friend.getUser2().getAccountID())
                .build();
    }

    private Specification<Friend> getAllFriendSpecification(SearchFriendRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Predicate predicate = criteriaBuilder.or(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("user1").get("accountID")),
                                request.getAccountID().toLowerCase()),
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("user2").get("accountID")),
                                request.getAccountID().toLowerCase())
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }

    private Specification<Friend> getFriendSpecification(SearchFriendRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Predicate predicate = criteriaBuilder.or(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        criteriaBuilder.lower(root.get("user1").get("accountID")),
                                        request.getAccountID().toLowerCase()),
                                criteriaBuilder.equal(
                                        criteriaBuilder.lower(root.get("user2").get("accountID")),
                                        request.getFriendID().toLowerCase())
                        ),
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        criteriaBuilder.lower(root.get("user2").get("accountID")),
                                        request.getAccountID().toLowerCase()),
                                criteriaBuilder.equal(
                                        criteriaBuilder.lower(root.get("user1").get("accountID")),
                                        request.getFriendID().toLowerCase())
                        )
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
