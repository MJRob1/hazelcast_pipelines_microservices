package hazelcast.microservices.common;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

import java.io.IOException;

public class Customer implements Portable {

    private int customerId;
    private  String firstName;
    private  String lastName;
    private  String companyName;
    private  String address;
    private  String city;
    private  String county;
    private  String postal;
    private  String phone1;
    private  String phone2;
    private  String email;
    private  String web;

  /*  public Customer(String firstName, String lastName, String companyName, String address, String city, String county,
                    String postal, String phone1, String phone2, String email, String web) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.address = address;
        this.city = city;
        this.county = county;
        this.postal = postal;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email = email;
        this.web = web;
    }  */

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getPostal() {
        return postal;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getEmail() {
        return email;
    }

    public String getWeb() {
        return web;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", postal='" + postal + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", email='" + email + '\'' +
                ", web='" + web + '\'' +
                '}';
    }

    @Override
    public int getFactoryId() {
        return MicroservicesPortableFactory.ID;
    }

    public static final int ID= 2;
    @Override
    public int getClassId() {
        return ID;
    }

    @Override
    public void writePortable(PortableWriter portableWriter) throws IOException {
        portableWriter.writeInt( "customerId", customerId);
        portableWriter.writeString("firstName", firstName);
        portableWriter.writeString("lastName", lastName);
        portableWriter.writeString("companyName", companyName);
        portableWriter.writeString("address", address);
        portableWriter.writeString("city", city);
        portableWriter.writeString("county", county);
        portableWriter.writeString("postal", postal);
        portableWriter.writeString("phone1", phone1);
        portableWriter.writeString("phone2", phone2);
        portableWriter.writeString("email", email);
        portableWriter.writeString("web", web);
    }

    @Override
    public void readPortable(PortableReader portableReader) throws IOException {
        this.customerId = portableReader.readInt("customerId");
        this.firstName = portableReader.readString("firstName");
        this.lastName = portableReader.readString("lastName");
        this.companyName = portableReader.readString("companyName");
        this.address = portableReader.readString("address");
        this.city = portableReader.readString("city");
        this.county = portableReader.readString("county");
        this.postal = portableReader.readString("postal");
        this.phone1 = portableReader.readString("phone1");
        this.phone2 = portableReader.readString("phone2");
        this.email = portableReader.readString("email");
        this.web = portableReader.readString("web");
    }
}
