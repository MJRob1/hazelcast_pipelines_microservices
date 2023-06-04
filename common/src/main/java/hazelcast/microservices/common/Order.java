package hazelcast.microservices.common;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Order implements Portable {

    private int orderId;

    private int customerId;

    private BigDecimal amount;

    private long eventTime;
    private  String firstName;
    private  String lastName;
    private  String companyName;
    private  String address;
    private  String city;
    private  String county;
    private  String postal;
    private  String phone1;
    private  String email;

   /*  public Order(Integer orderId, Integer customerId, Double amount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
    }   */

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", amount=" + amount +
                '}';
    }

    private static int MIN = 1;
    private static int MAX = 501;
    public static Order fake(int orderId){
        Order result = new Order();

        // Generate random integer between 0 and 500 for customer ID
        // and random decimal < 100 for order price
        result.setOrderId(orderId);
        result.setCustomerId((int)(Math.random()*(MAX-MIN+1)+MIN));
        result.setAmount((BigDecimal.valueOf(Math.random()*100)).setScale(2, RoundingMode.DOWN));
        result.setEventTime(System.currentTimeMillis());

        return result;
    }
    public static int ID = 1;
    @Override
    public int getFactoryId() {
        return MicroservicesPortableFactory.ID;
    }

    @Override
    public int getClassId() {
        return ID;
    }

    @Override
    public void writePortable(PortableWriter portableWriter) throws IOException {
        portableWriter.writeInt( "orderId", orderId);
        portableWriter.writeInt( "customerId", customerId);
        portableWriter.writeDecimal("amount", amount);
        portableWriter.writeLong("eventTime", eventTime);
        portableWriter.writeString("firstName", firstName);
        portableWriter.writeString("lastName", lastName);
        portableWriter.writeString("companyName", companyName);
        portableWriter.writeString("address", address);
        portableWriter.writeString("city", city);
        portableWriter.writeString("county", county);
        portableWriter.writeString("postal", postal);
        portableWriter.writeString("phone1", phone1);
        portableWriter.writeString("email", email);
    }

    @Override
    public void readPortable(PortableReader portableReader) throws IOException {
        this.orderId = portableReader.readInt("orderId");
        this.customerId = portableReader.readInt("customerId");
        this.amount = portableReader.readDecimal("amount");
        this.eventTime = portableReader.readLong("eventTime");
        this.firstName = portableReader.readString("firstName");
        this.lastName = portableReader.readString("lastName");
        this.companyName = portableReader.readString("companyName");
        this.address = portableReader.readString("address");
        this.city = portableReader.readString("city");
        this.county = portableReader.readString("county");
        this.postal = portableReader.readString("postal");
        this.phone1 = portableReader.readString("phone1");
        this.email = portableReader.readString("email");
    }
}
