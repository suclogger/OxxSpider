package com.suclogger.repository;

import com.suclogger.domain.Oxx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by suclogger on 2017/4/6.
 */
public interface OxxRepository extends JpaRepository<Oxx, Long> {
    List<Oxx> findOxxesByVideoId(String videoId);
}
