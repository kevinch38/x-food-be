package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.feature.user.UserRepository;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.enigma.x_food.util.SortingUtil;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final BCryptUtil bCryptUtil;

    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse createNew(NewUserRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);
            User user = User.builder()
                    .ktpID("")
                    .accountEmail(request.getAccountEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .pinID(request.getPinID())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .dateOfBirth(LocalDate.of(1970,1,1))
                    .balanceID(request.getBalanceID())
                    .loyaltyPointID(request.getLoyaltyPointID())
                    .otpID("")
                    .build();
            userRepository.saveAndFlush(user);
            log.info("End createNew");
            return mapToResponse(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAll(SearchUserRequest request) {
        log.info("Start getAll");
        String fieldName = SortingUtil.sortByValidation(User.class, request.getSortBy(), "accountID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Specification<User> specification = getUserSpecification(request);
        Page<User> users = userRepository.findAll(specification, pageable);
        log.info("End getAll");
        return users.map(this::mapToResponse);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .accountID(user.getAccountID())
                .ktpID(user.getKtpID())
                .accountEmail(user.getAccountEmail())
                .phoneNumber(user.getPhoneNumber())
                .pinID(user.getPinID())
                .createdAt(user.getCreatedAt())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().toString())
                .updatedAt(user.getUpdatedAt())
                .balanceID(user.getBalanceID())
                .loyaltyPointID(user.getLoyaltyPointID())
                .otpID(user.getOtpID())
                .build();
    }

    private Specification<User> getUserSpecification(SearchUserRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("accountID")),
                        "%" + request.getAccountID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getAccountEmail() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("accountEmail")),
                        "%" + request.getAccountEmail().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getFirstName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        "%" + request.getFirstName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getLastName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        "%" + request.getLastName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPhoneNumber() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("phoneNumber")),
                        "%" + request.getPhoneNumber().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
