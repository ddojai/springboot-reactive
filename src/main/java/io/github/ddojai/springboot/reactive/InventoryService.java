package io.github.ddojai.springboot.reactive;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class InventoryService {

    private final ItemRepository repository;
    private ReactiveFluentMongoOperations fluentOperations;


    public InventoryService(ItemRepository repository) {
        this.repository = repository;
    }

    Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny())
            .withStringMatcher(StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, matcher);

        return repository.findAll(probe);
    }

    Flux<Item> searchByFluentExample(String name, String description) {
        return fluentOperations.query(Item.class)
            .matching(query(where("TV tray").is(name).and("Smurf").is(description)))
            .all();
    }

    // tag::code-5[]
    Flux<Item> searchByFluentExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (useAnd //
            ? ExampleMatcher.matchingAll() //
            : ExampleMatcher.matchingAny()) //
            .withStringMatcher(StringMatcher.CONTAINING) //
            .withIgnoreCase() //
            .withIgnorePaths("price");

        return fluentOperations.query(Item.class) //
            .matching(query(byExample(Example.of(item, matcher)))) //
            .all();
    }
    // end::code-5[]
}
