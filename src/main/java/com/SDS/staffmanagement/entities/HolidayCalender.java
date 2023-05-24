package com.SDS.staffmanagement.entities;

import javax.persistence.*;

@Entity
@Table
public class HolidayCalender {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String date;
    private String remark;

    public HolidayCalender() {
    }

    @Override
    public String toString() {
        return "Calender{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public HolidayCalender(int id, String date, String remark) {
        this.id = id;
        this.date = date;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
