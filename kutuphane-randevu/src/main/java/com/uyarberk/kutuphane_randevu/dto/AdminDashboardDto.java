package com.uyarberk.kutuphane_randevu.dto;

public class AdminDashboardDto {

    private long totalUsers;
    private long totalAppointments;

    public AdminDashboardDto(long totalUsers, long totalAppointments, long todayAppointments, long availableRooms, String mostPopularHour) {
        this.totalUsers = totalUsers;
        this.totalAppointments = totalAppointments;
        this.todayAppointments = todayAppointments;
        this.availableRooms = availableRooms;
        this.mostPopularHour = mostPopularHour;
    }
     public AdminDashboardDto(){

      }
    private long todayAppointments;
    private long availableRooms;

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public long getTodayAppointments() {
        return todayAppointments;
    }

    public void setTodayAppointments(long todayAppointments) {
        this.todayAppointments = todayAppointments;
    }

    public long getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(long availableRooms) {
        this.availableRooms = availableRooms;
    }

    public String getMostPopularHour() {
        return mostPopularHour;
    }

    public void setMostPopularHour(String mostPopularHour) {
        this.mostPopularHour = mostPopularHour;
    }

    private String mostPopularHour;

}
