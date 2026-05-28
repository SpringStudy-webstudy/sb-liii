package com.study.springbootstudy.service;

import com.study.springbootstudy.dto.ExternalTodoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "todoCache", key = "#id")
    public ExternalTodoResponse getExternalTodo(Long id) {
        // 캐시 테스트 확인을 위해 메서드 실행 시 로그를 출력하도록 설정합니다.
        // 데이터가 캐시에 있으면 이 메서드 자체가 실행되지 않으므로 로그도 찍히지 않습니다.
        log.info("외부 API를 호출합니다. ID: {}", id);

        String url = "https://jsonplaceholder.typicode.com/todos/" + id;
        return restTemplate.getForObject(url, ExternalTodoResponse.class);
    }
}