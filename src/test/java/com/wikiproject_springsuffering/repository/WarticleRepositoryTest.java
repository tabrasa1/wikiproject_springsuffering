package com.wikiproject_springsuffering.repository;

import com.wikiproject_springsuffering.model.Warticle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WarticleRepositoryTest {

    @Autowired
    private WarticleRepository warticleRepo;
    //We're using warticleRepository because that's
    //the schema where articles live

    @Test
    public void findTop3ByOrderByDateCreateDesc_returnsLatestThree() throws InterruptedException {

        // create 4 skeleton articles with incremental timestamps
        Warticle first = new Warticle();
        first.setTitle("one"); first.setDateCreate(new Timestamp(System.currentTimeMillis()));
        warticleRepo.save(first);
        Thread.sleep(10);
        Warticle second = new Warticle();
        second.setTitle("two"); second.setDateCreate(new Timestamp(System.currentTimeMillis()));
        warticleRepo.save(second);
        Thread.sleep(10);
        Warticle third = new Warticle();
        third.setTitle("three"); third.setDateCreate(new Timestamp(System.currentTimeMillis()));
        warticleRepo.save(third);
        Thread.sleep(10);
        Warticle fourth = new Warticle();
        fourth.setTitle("four"); fourth.setDateCreate(new Timestamp(System.currentTimeMillis()));
        warticleRepo.save(fourth);

        List<Warticle> top3 = warticleRepo.findTop3ByOrderByDateCreateDesc();
        assertThat(top3).hasSize(3);
        // verify the titles in order for the three most-recent entries
        List<String> expected = List.of("four", "three", "two");
        for (int i = 0; i < expected.size(); i++) {
            assertThat(top3.get(i).getTitle()).isEqualTo(expected.get(i));
        }
    }
}
