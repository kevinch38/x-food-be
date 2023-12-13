package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.feature.pin.PinService;
import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.request.UpdateUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
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
    private final PinService pinService;

    private final BCryptUtil bCryptUtil;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse createNew(NewUserRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);
            Pin pin = pinService.createNew(NewPinRequest.builder().pin("").build());
            User user = User.builder()
                    .ktpID("")
                    .accountEmail(request.getAccountEmail())
                    .pin(pin)
                    .phoneNumber(request.getPhoneNumber())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .dateOfBirth(LocalDate.of(1970,1,1))
                    .balanceID("")
                    .loyaltyPointID("")
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

    @Override
    public UserResponse getById(String id) {
        log.info("Start getOneById");
        User user = findByIdOrThrowNotFound(id);
        log.info("End getOneById");
        return mapToResponse(user);
    }

    @Override
    public User getUserById(String id) {
        return (findByIdOrThrowNotFound(id));
    }

    @Override
    public UserResponse update(UpdateUserRequest request) {
        try {
            log.info("Start update");
            validationUtil.validate(request);
            User user = findByIdOrThrowNotFound(request.getAccountID());
            user.setKtpID(request.getKtpID());
            user.setAccountEmail(request.getAccountEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setDateOfBirth(request.getDateOfBirth());
            userRepository.saveAndFlush(user);
            log.info("End update");
            return mapToResponse(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Error update: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }

    @Override
    public void deleteById(String id) {
        log.info("Start deleteById");
        User user = findByIdOrThrowNotFound(id);
        userRepository.delete(user);
        log.info("End deleteById");
    }


    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .accountID(user.getAccountID())
                .ktpID(user.getKtpID())
                .accountEmail(user.getAccountEmail())
                .phoneNumber(user.getPhoneNumber())
                .pinID(user.getPin().getPinID())
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

    private User findByIdOrThrowNotFound(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
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
