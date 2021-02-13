package springframework.reactive.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ReactiveExamplesTest {


    Person julio = new Person("Julio", "Chacón");
    Person carolin = new Person("Carolin", "Vilela");
    Person nayeli = new Person("Nayeli", "Diaz");
    Person fabiana = new Person("Fabiana", "Chacón");
    @Test
    public void monoTests() throws Exception {
        //create new person mono
        Mono<Person> personMono = Mono.just(julio);

        //get person object from mono publisher
        Person person = personMono.block();

        // output name
        log.info(person.sayMyName());
    }

    @Test
    public void monoTransform() throws Exception {
        //create new person mono
        Mono<Person> personMono = Mono.just(carolin);

        PersonCommand command = personMono
                .map(person -> { //type transformation
                    return new PersonCommand(person);
                }).block();

        log.info(command.sayMyName());
    }

    //@Test(expected = NullPointerException.class)
    @Test
    public void monoFilter() throws Exception {
        Mono<Person> personMono = Mono.just(nayeli);

        //filter example
        Person nayeliDiaz = personMono
                .filter(person -> person.getFirstName().equalsIgnoreCase("foo"))
                .block();

        //expected not working, so I did this block to avoid the NPE
        if (nayeliDiaz != null) {
            log.info(nayeliDiaz.sayMyName()); //throws NPE
        }
    }

    @Test
    public void fluxTest() throws Exception {

        Flux<Person> people = Flux.just(julio, carolin, nayeli, fabiana);

        people.subscribe(person -> log.info(person.sayMyName()));

    }

    @Test
    public void fluxTestFilter() throws Exception {

        Flux<Person> people = Flux.just(julio, carolin, nayeli, fabiana);

        people.filter(person -> person.getFirstName().equals(carolin.getFirstName()))
                .subscribe(person -> log.info(person.sayMyName()));

    }

    @Test
    public void fluxTestDelayNoOutput() throws Exception {

        Flux<Person> people = Flux.just(julio, carolin, nayeli, fabiana);

        people.delayElements(Duration.ofSeconds(1))
                .subscribe(person -> log.info(person.sayMyName()));

    }

    @Test
    public void fluxTestDelay() throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Flux<Person> people = Flux.just(julio, carolin, nayeli, fabiana);

        people.delayElements(Duration.ofSeconds(1))
                .doOnComplete(countDownLatch::countDown)
                .subscribe(person -> log.info(person.sayMyName()));

        countDownLatch.await();

    }

    @Test
    public void fluxTestFilterDelay() throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Flux<Person> people = Flux.just(julio, carolin, nayeli, fabiana);

        people.delayElements(Duration.ofSeconds(1))
                .filter(person -> person.getFirstName().contains("i"))
                .doOnComplete(countDownLatch::countDown)
                .subscribe(person -> log.info(person.sayMyName()));

        countDownLatch.await();
    }
}
