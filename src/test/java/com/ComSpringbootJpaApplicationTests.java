package com;

import com.entity.User;
import com.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@SpringBootTest
class ComSpringbootJpaApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        List<User> users = userRepository.findAll();
        users.stream().forEach(e -> System.out.println(e));
    }

    @Test
    void contextLoads02() {
        Specification<User> spec = ((root, cq, cb) -> cb.ge(root.get("id"),1));
        Sort sort = Sort.by(Sort.Direction.ASC, "age");
        Pageable pageable = PageRequest.of(0, 3, sort);
        Page<User> users = userRepository.findAll(spec, pageable);
        users.stream().forEach( e -> System.out.println(e));
    }
}
