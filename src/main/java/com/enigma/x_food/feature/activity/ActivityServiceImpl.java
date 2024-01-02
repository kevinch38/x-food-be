package com.enigma.x_food.feature.activity;

import com.enigma.x_food.constant.EActivity;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void init() {
        List<Activity> activity = new ArrayList<>();
        for (EActivity value : EActivity.values()) {
            Optional<Activity> status = activityRepository.findByActivity(value);
            if (status.isPresent()) continue;

            activity.add(Activity.builder()
                    .activity(value)
                    .build());
        }
        activityRepository.saveAllAndFlush(activity);
    }

    @Transactional(readOnly = true)
    @Override
    public Activity findByActivity(EActivity status) {
        return findByActivityOrThrowNotFound(status);
    }
    private Activity findByActivityOrThrowNotFound(EActivity status) {
        return activityRepository.findByActivity(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant branch status not found"));
    }
}
