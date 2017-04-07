package com.suclogger.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by suclogger on 2017/4/6.
 */
@Entity
@Table(name = "oxx")
@Getter
@Setter
public class Oxx {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String videoId;

    private String title;

    private String description;

    private String length;

    private int viewTimes;

    private Date gmtCreate;

    public Oxx() {
    }

    public Oxx(String videoId, String title, String description, String length, int viewTimes, Date gmtCreate) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.length = length;
        this.viewTimes = viewTimes;
        this.gmtCreate = gmtCreate;
    }
}
