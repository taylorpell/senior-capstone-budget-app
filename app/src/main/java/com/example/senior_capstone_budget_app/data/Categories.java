package com.example.senior_capstone_budget_app.data;

public enum Categories {
    UNCATEGORIZED(0),
    RENT(1),
    UTILITIES(2),
    HOUSEHOLD(4),
    PERSONAL(5),
    MEDICAL(6),
    FINANCIAL(8);


    private int value;

    private Categories(int value){
        this.value = value;
    }

    public int getVal() {
        return this.value;
    }
}
