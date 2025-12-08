package com.example.sanisidropharmacy;

import java.util.List;

public class AdminLoyaltySummaryResponse {
    private boolean success;
    private int total_customers;
    private long total_points;
    private List<TopCustomer> top_customers;

    public boolean isSuccess() { return success; }
    public int getTotal_customers() { return total_customers; }
    public long getTotal_points() { return total_points; }
    public List<TopCustomer> getTop_customers() { return top_customers; }

    public static class TopCustomer {
        public int user_id;
        public String fullname;
        public int loyalty_points;
    }
}
