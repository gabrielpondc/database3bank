//20161795GUJIAKAI
package jdbc;
import java.sql.*;
import java.util.Scanner;
class Account {
    int acc_id;
    double balance;
    public Account(int acc_id, double balance) {
        this.acc_id = acc_id;
        this.balance = balance;
    }
    public int getAcc_id() {return acc_id;}
    public double getBalnace() {return balance;}
    public synchronized double take(double money) {
        if (balance >= money) {
            balance -= money;
            return money;
        }
        money = balance;
        balance = 0;
        return money;
    }
    public synchronized void save(double money) {
        if (money < 0) {
            return;
        }
        balance += money;
    }
}
public class jdbcc {
    static int acc1=0,acc2=0;
    static double b1=0;
    static double b2=0;
    static double amount=0;
    static final String DB_URL="jdbc:mysql://gabrielpondc.softether.net:3306/test?true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String USER="root";
    static final String PASS="gjk19961226";
    static final String useUnicode="";
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();
            Scanner input=new Scanner(System.in);
            System.out.print("Please input withdraw account:");
            acc1=input.nextInt(); //输入整型
            String sql="select * from account where acct_no="+acc1;
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                double balance1 =resultSet.getDouble("balance");
                b1= (float) balance1;
            }
            resultSet.close();
            System.out.print("Please input deposit account:");
            acc2=input.nextInt();
            String sql1="select * from account where acct_no="+acc2;
            ResultSet resultSet2 = statement.executeQuery(sql1);
            while(resultSet2.next()) {
                double balance2 = resultSet2.getDouble("balance");
                b2=balance2;
            }
            Account a1 = new Account(acc1, b1);
            Account a2 = new Account(acc2, b2);
            System.out.print("Please input amount needed transfer:");
            amount=input.nextDouble();
            transfer(a1, a2,amount);
            String sqlup1="update account set balance="+a1.balance+" where acct_no="+a1.acc_id;
            statement.execute(sqlup1);
            String sqlup2="update account set balance="+a2.balance+" where acct_no="+a2.acc_id;
            statement.execute(sqlup2);
            resultSet2.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static boolean transfer(Account from, Account to, double money) {
        if (money > from.getBalnace()) {
            System.out.printf("Error:Account %s has no enough money to transfer.\n", from.getAcc_id());
            return false;
        }
        to.save(from.take(money));
        System.out.printf("Success!!Account %s transfers money[%.2f] to account %s.\n", from.getAcc_id(), money, to.getAcc_id());
        return true;
    }
}
