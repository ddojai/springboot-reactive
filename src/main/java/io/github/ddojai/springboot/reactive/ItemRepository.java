package io.github.ddojai.springboot.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository extends ReactiveCrudRepository<Item, String> {

}
