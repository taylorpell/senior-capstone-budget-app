package com.example.senior_capstone_budget_app.transaction;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import com.example.senior_capstone_budget_app.data.database.DBConnectorInterface;
import com.example.senior_capstone_budget_app.data.database.DataStoreAdapter;
import com.example.senior_capstone_budget_app.data.database.MySQLDatabase;

import java.sql.Timestamp;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarOutputStream;


public class MonthlyTransactions extends AppCompatActivity{
    //Example transaction set for uses when testing the class without context passing issues
    String[] temp = new String[]{"-100.00,Amazon,1617249600000,0",
            "-60.42,Harris Teeter,1617249600000,4",
            "-7.82,McDonald's,1617336000000,4",
            "-25.68,Shell,1617336000000,3",
            "-450.00,Keystone,1617595200000,1",
            "-45.97,Amazon,1618286400000,5",
            "-200.00,USAA,1618459200000,8",
            "-34.76,Harris Teeter,1618459200000,4",
            "-100.00,IRS,1618718400000,7",
            "-12.32,Walgreens,1618977600000,6",
            "-73.45,Duke Power,1619496000000,2"};

    String[] monthNames = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};

    private Context context;
    private Date currentMonth;
    private Date nextMonth;

    private Timestamp currentTimestamp;
    private Timestamp nextTimestamp;
    private Timestamp timestampMinus1;
    private Timestamp timestampMinus2;
    private Timestamp timestampMinus3;
    private Timestamp timestampMinus4;
    private Timestamp timestampMinus5;

    private ArrayList<Transaction> currentTransactions;
    private ArrayList<Transaction> allTransactions;

    private double total = 0;
    private double minus1Month = 0;
    private double minus2Month = 0;
    private double minus3Month = 0;
    private double minus4Month = 0;
    private double minus5Month = 0;

    private ArrayList<Double> totals;
    private ArrayList<String> history;
    private double[] categoryTotals;
    private int[] categoryPercents;
    private DataStoreAdapter arvioDatabase;

    /**
     * MonthlyTransaction object constructor with no context passed
     */
    public MonthlyTransactions() {
        this.history = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        currentMonth = cal.getTime();
        int now = cal.MONTH;
        currentTimestamp = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now + 1+12)%12]);

        cal.add(Calendar.MONTH, 1);
        nextMonth = cal.getTime();
        nextTimestamp = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -2);
        timestampMinus1 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now +12)%12]);

        cal.add(Calendar.MONTH, -1);
        timestampMinus2 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now - 1+12)%12]);

        cal.add(Calendar.MONTH, -1);
        timestampMinus3 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now - 2+12)%12]);

        cal.add(Calendar.MONTH, -1);
        timestampMinus4 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now - 3+12)%12]);

        cal.add(Calendar.MONTH, -1);
        timestampMinus5 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now - 4+12)%12]);

        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.currentTransactions = new ArrayList<>();
        this.totals = new ArrayList<>();
        this.allTransactions = new ArrayList<>();
        this.arvioDatabase = new DataStoreAdapter();
    }

    /**
     * Monthly Transaction Object constructor for use if context is passed
     * @param c Android application context
     */
    public MonthlyTransactions(Context c) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        currentMonth = cal.getTime();
        currentTimestamp = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, 1);

        nextMonth = cal.getTime();
        nextTimestamp = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -2);
        timestampMinus1 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus2 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus3 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus4 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus5 = new Timestamp(cal.getTimeInMillis());

        this.context = c;
        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.currentTransactions = new ArrayList<>();
        this.totals = new ArrayList<>();
    }

    /**
     * Creates a MonthlyTransactions object for use when populating the transactions history view
     * @param date The start date of the month we want history for
     */
    public void MonthlyTransactionsHistory(Date date) {
        this.currentMonth = date;
        currentTimestamp = new Timestamp(currentMonth.getTime());

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentMonth);
        cal.add(Calendar.MONTH, 1);

        nextMonth = cal.getTime();
        nextTimestamp = new Timestamp(cal.getTimeInMillis());

        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.currentTransactions = new ArrayList<>();
    }

    /**
     * !!! Modify for database when ready
     * For reading from internal array
     * Deprecated
     */
    public void loadTransactions(){
        int counter = 0;
        for (String s: temp){
            String[] t = s.split(",");
            double amount = Double.parseDouble(t[0]);
            long l = Long.parseLong(t[2]);
            Timestamp time = new Timestamp(l);
            int cat = Integer.parseInt(t[3]);

            if(currentTimestamp.compareTo(time)<= 0 && nextTimestamp.compareTo(time)>0){
                currentTransactions.add(counter, new Transaction(amount, t[1], time, cat));
                counter ++;
            }
        }
    }


    /**
     * For use when reading from a file/database
     * '\n' separates each transaction ',' separates each transaction pieces
     * @param input File/Database input as a string
     */
    public void loadTransactions2(String input){
        String[] split = input.split("\n");
        int currentCounter = 0;
        ArrayList<Transaction> oldTransactions = new ArrayList<>();

        for (String s: split){
            s = s.replaceAll("[^\\x00-\\x7F]", "");
            s = s.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            s = s.replaceAll("\\p{C}", "");
            String[] t = s.split(",");
            double amount = Double.parseDouble(t[0]);
            long l = Long.parseLong(t[2]);
            Timestamp time = new Timestamp(l);
            int cat = Integer.parseInt(t[3]);
            if(currentTimestamp.compareTo(time)<= 0 && nextTimestamp.compareTo(time)>0){
                currentTransactions.add(currentCounter, new Transaction(amount, t[1], time, cat));
                total += -1 * amount;
                currentCounter ++;
            }
            if(timestampMinus1.compareTo(time)<=0 && currentTimestamp.compareTo(time)>0){
                minus1Month += -1 * amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
            if(timestampMinus2.compareTo(time)<=0 &&  timestampMinus1.compareTo(time)>0){
                minus2Month += -1 * amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
            if(timestampMinus3.compareTo(time)<=0 &&  timestampMinus2.compareTo(time)>0){
                minus3Month += -1 * amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
            if(timestampMinus4.compareTo(time)<=0 &&  timestampMinus3.compareTo(time)>0){
                minus4Month += -1 * amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
            if(timestampMinus5.compareTo(time)<=0 &&  timestampMinus4.compareTo(time)>0){
                minus5Month += -1 * amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
        }
        allTransactions.addAll(currentTransactions);
        allTransactions.addAll(oldTransactions);

        totals.add(total);
        totals.add(minus1Month);
        totals.add(minus2Month);
        totals.add(minus3Month);
        totals.add(minus4Month);
        totals.add(minus5Month);

        //loadFromDatabase();
    }

    public void loadFromDatabase(){
        Map<String, String> user = new HashMap<>();
        user.put("UUID", "b356ab0b-69d5-4483-b855-af29a48e5148");

    }

    /**
     * Loop trough the transaction list to calculate the total spent, total spent per category,
     * and the percent spent per category.
     */
    public void transactionLoop(){
        total = 0;
        double starting;
        double ending;

        for (Transaction t : currentTransactions){
            double a = -1.0 * t.getAmount();
            total += a;
            switch (t.getCategory()) {
                case UNCATEGORIZED:
                    starting = categoryTotals[0];
                    ending = starting + a;
                    categoryTotals[0] = ending;
                    break;
                case RENT:
                    starting = categoryTotals[1];
                    ending = starting + a;
                    categoryTotals[1] = ending;
                    break;
                case UTILITIES:
                    starting = categoryTotals[2];
                    ending = starting + a;
                    categoryTotals[2] = ending;
                    break;
                case HOUSEHOLD:
                    starting = categoryTotals[4];
                    ending = starting + a;
                    categoryTotals[4] = ending;
                    break;
                case PERSONAL:
                    starting = categoryTotals[5];
                    ending = starting + a;
                    categoryTotals[5] = ending;
                    break;
                case MEDICAL:
                    starting = categoryTotals[6];
                    ending = starting + a;
                    categoryTotals[6] = ending;
                    break;
                case FINANCIAL:
                    starting = categoryTotals[8];
                    ending = starting + a;
                    categoryTotals[8] = ending;
                    categoryPercents[8] = (int) ((ending / total) * 100);
                    break;
            }
            calculatePercents();
        }
    }

    public void categorizeTransaction(Transaction t){
        double starting;
        double ending;
        double a = 0.0;

        if (t.getAmount() <= 0 ){
            a = -1.0 * t.getAmount();

        }else{
            a = t.getAmount();
        }

        switch (t.getCategory()) {
            case UNCATEGORIZED:
                starting = categoryTotals[0];
                ending = starting + a;
                categoryTotals[0] = ending;
                break;
            case RENT:
                starting = categoryTotals[1];
                ending = starting + a;
                categoryTotals[1] = ending;
                break;
            case UTILITIES:
                starting = categoryTotals[2];
                ending = starting + a;
                categoryTotals[2] = ending;
                break;
            case HOUSEHOLD:
                starting = categoryTotals[4];
                ending = starting + a;
                categoryTotals[4] = ending;
                break;
            case PERSONAL:
                starting = categoryTotals[5];
                ending = starting + a;
                categoryTotals[5] = ending;
                break;
            case MEDICAL:
                starting = categoryTotals[6];
                ending = starting + a;
                categoryTotals[6] = ending;
                break;
            case FINANCIAL:
                starting = categoryTotals[8];
                ending = starting + a;
                categoryTotals[8] = ending;
                categoryPercents[8] = (int) ((ending / total) * 100);
                break;
        }
        calculatePercents();
    }

    /**
     * Add a single transaction to transactions list
     * @param t the transaction object to be added
     */
    public void addTransaction(double d, String payee, Timestamp t, int c){
        Transaction trans = new Transaction(d, payee, t, c);

        if(currentTimestamp.compareTo(t)<= 0 && nextTimestamp.compareTo(t)>0){
            currentTransactions.add(trans);
            if (d <= 0 ){
                total += -1 * d;

            }else{
                total += d;
            }
            totals.set(0,total);
        }
        if(timestampMinus1.compareTo(t)<=0 && currentTimestamp.compareTo(t)>0){
            minus1Month = totals.get(1);
            if (d <= 0 ){
                minus1Month += -1 * d;

            }else{
                minus1Month += d;
            }
            totals.set(1,minus1Month);
        }
        if(timestampMinus2.compareTo(t)<=0 &&  timestampMinus1.compareTo(t)>0){
            minus2Month = totals.get(2);
            if (d <= 0 ){
                minus2Month += -1 * d;

            }else{
                minus2Month += d;
            }
            totals.set(2,minus2Month);
        }
        if(timestampMinus3.compareTo(t)<=0 &&  timestampMinus2.compareTo(t)>0){
            minus3Month = totals.get(3);
            if (d <= 0 ){
                minus3Month += -1 * d;

            }else{
                minus3Month += d;
            }
            totals.set(3,minus3Month);
        }
        if(timestampMinus4.compareTo(t)<=0 &&  timestampMinus3.compareTo(t)>0){
            minus4Month = totals.get(4);
            if (d <= 0 ){
                minus4Month += -1 * d;

            }else{
                minus4Month += d;
            }
            totals.set(4,minus4Month);
        }
        if(timestampMinus5.compareTo(t)<=0 &&  timestampMinus4.compareTo(t)>0){
            minus5Month = totals.get(5);
            if (d <= 0 ){
                minus5Month += -1 * d;

            }else{
                minus5Month += d;
            }
            totals.set(5,minus5Month);
        }
        categorizeTransaction(trans);
        allTransactions.add(trans);
    }

    /**
     * Remove a specifc transaction from the transactions list
     * @param index the  integer index of the transaction to be removed
     */
    public void removeTransaction(int index){
        System.out.println(currentTransactions.size());
        Transaction t = currentTransactions.get(index);
        double a = t.getAmount();
        int cat = t.getCategory().getVal();
        System.out.println(currentTransactions.get(index).toString());
        System.out.println(allTransactions.get(index).toString());
        currentTransactions.remove(index);
        allTransactions.remove(index);
        if (a <= 0 ){
            total -= -1 * a;
            categoryTotals[cat] -= -1 * a;
        }else{
            total -= a;
            categoryTotals[cat] -= a;
        }
        calculatePercents();
    }

    @Override
    public String toString() {
        String transactions = "";

        for (Transaction t: allTransactions){
            transactions = transactions + t.toString() + "\n";
        }

        return transactions;
    }

    private void calculatePercents(){
        for (int k = 0; k < 9; k++){
            double catTotal = categoryTotals[k];
            categoryPercents[k] = (int) ((catTotal/total) * 100);
        }
    }

    //====================================Getters====================================//
    public Date getCurrentMonth() {return currentMonth;}
    public Date getNextMonth() {return nextMonth;}
    public Timestamp getCurrentTimestamp() {return currentTimestamp;}
    public Timestamp getNextTimestamp() {return nextTimestamp;}
    public ArrayList<Transaction> getCurrentTransactions() {return currentTransactions;}
    public Transaction getTransaction(int index) {return currentTransactions.get(index);}
    public double getTotal() {return total;}
    public ArrayList<Double> getTotals() {return totals;}
    public int getCategoryPercents(int index) {return categoryPercents[index];}
    public int getLength(){return currentTransactions.size();}
    public String getHistory(int index) {return history.get(index);}

    //====================================Setters====================================//
    public void setCurrentMonth(Date currentMonth) {this.currentMonth = currentMonth;}
    public void setNextMonth(Date nextMonth) {this.nextMonth = nextMonth;}
    public void setCurrentTimestamp(Timestamp currentTimestamp) {this.currentTimestamp = currentTimestamp;}
    public void setNextTimestamp(Timestamp nextTimestamp) {this.nextTimestamp = nextTimestamp;}
    public void setCurrentTransactions(ArrayList<Transaction> currentTransactions) {
        this.currentTransactions = currentTransactions;
    }
    public void setTotal(double total) {this.total = total;}
    public void setMonthlyTransactions(Transaction t, int index) {
        this.currentTransactions.set(index, t);
    }
}
