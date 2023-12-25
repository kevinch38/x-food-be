package com.enigma.x_food.feature.method;

import com.enigma.x_food.constant.EMethod;
import com.enigma.x_food.feature.method.response.MethodResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MethodServiceImpl implements MethodService {
    private final MethodRepository methodRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void init() {
        List<Method> method = new ArrayList<>();
        for (EMethod value : EMethod.values()) {
            Optional<Method> name = methodRepository.findByMethodName(value);
            if (name.isPresent()) continue;

            method.add(Method.builder()
                    .methodName(value)
                    .build());
        }
        methodRepository.saveAllAndFlush(method);
    }

    @Transactional(readOnly = true)
    @Override
    public Method getByMethodName(EMethod name) {
        return findByStatusOrThrowNotFound(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MethodResponse> getAll() {
        log.info("Start getAll");

        List<Method> method = methodRepository.findAll();
        log.info("End getAll");
        return method.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private Method findByStatusOrThrowNotFound(EMethod name) {
        return methodRepository.findByMethodName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "method name not found"));
    }

    private MethodResponse mapToResponse(Method method) {
        return MethodResponse.builder()
                .methodID(method.getMethodID())
                .methodName(method.getMethodName().toString())
                .build();
    }
}
