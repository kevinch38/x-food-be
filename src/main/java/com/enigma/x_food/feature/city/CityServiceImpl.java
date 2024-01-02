package com.enigma.x_food.feature.city;

import com.enigma.x_food.feature.city.dto.response.CityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void init() throws IOException {
        InputStream inp = new FileInputStream("src/main/java/com/enigma/x_food/shared/Status and City List.xlsx");
        Workbook workbook = new XSSFWorkbook(inp);
        Sheet sheet = workbook.getSheet("List of City");

        for (Row currentRow : sheet) {
            Iterator<Cell> cellsInRow = currentRow.iterator();
            String cityName;
            City city;
            List<City> cities = new ArrayList<>();
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();

                cityName = currentCell.getStringCellValue();

                Optional<City> byCityName = cityRepository.findByCityName(cityName);
                if (byCityName.isPresent()) continue;

                city = City.builder().cityName(cityName).build();
                cities.add(city);
            }

            workbook.close();
            cityRepository.saveAllAndFlush(cities);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public CityResponse getById(String id) {
        return mapToResponse(findByIdOrThrowNotFound(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityResponse> getAll() {
        log.info("Start getAll");

        List<City> cities = cityRepository.findAll();
        log.info("End getAll");
        return cities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private City findByIdOrThrowNotFound(String id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "city not found"));
    }

    private CityResponse mapToResponse(City city) {
        return CityResponse.builder()
                .cityID(city.getCityID())
                .cityName(city.getCityName())
                .createdAt(city.getCreatedAt())
                .updatedAt(city.getUpdatedAt())
                .build();
    }
}
