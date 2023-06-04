package hazelcast.microservices.client;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import hazelcast.microservices.common.Customer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RefDataLoader {

    private static final String CUSTOMERS_MAP = "customer-map" ;
    private static final String ORDERS_MAP = "orders-map" ;
    private static final String ORDERS_WITH_CUSTOMERS_MAP = "orders-with-customers-map" ;
    private static Integer CUSTOMER_ID = 1;

    private static final String CUSTOMER_MAPPING_SQL =  "CREATE OR REPLACE MAPPING " + "\"" + CUSTOMERS_MAP+ "\" " +
            "(customerId INTEGER, " +
            "firstName VARCHAR, " +
            "lastName VARCHAR, " +
            "companyName VARCHAR, " +
            "address VARCHAR, " +
            "city VARCHAR, " +
            "county VARCHAR, " +
            "postal VARCHAR, " +
            "phone1 VARCHAR, " +
            "phone2 VARCHAR, " +
            "email VARCHAR, " +
            "web VARCHAR) " +
            "TYPE IMap OPTIONS (" +
            "'keyFormat' = 'java'," +
            "'keyJavaClass' = 'java.lang.Integer'," +
            "'valueFormat' = 'compact'," +
            "'valueCompactTypeName' = 'hazelcast.microservices.common.Customer')";

    private static final String ORDERS_WITH_CUSTOMERS_MAPPING_SQL =  "CREATE OR REPLACE MAPPING " + "\"" + ORDERS_WITH_CUSTOMERS_MAP+ "\" " +
            "(orderId INTEGER, " +
            "customerId INTEGER, " +
            "amount DECIMAL, " +
            "eventTime BIGINT, " +
            "firstName VARCHAR, " +
            "lastName VARCHAR, " +
            "companyName VARCHAR, " +
            "address VARCHAR, " +
            "city VARCHAR, " +
            "county VARCHAR, " +
            "postal VARCHAR, " +
            "phone1 VARCHAR, " +
            "email VARCHAR) " +
            "TYPE IMap OPTIONS (" +
            "'keyFormat' = 'java'," +
            "'keyJavaClass' = 'java.lang.Integer'," +
            "'valueFormat' = 'compact'," +
            "'valueCompactTypeName' = 'hazelcast.microservices.common.Order')";

    private static final String ORDER_MAPPING_SQL =  "CREATE OR REPLACE MAPPING " + "\"" + ORDERS_MAP+ "\" " +
            "(orderId INTEGER, " +
            "customerId INTEGER, " +
            "amount DECIMAL, " +
            "eventTime BIGINT, " +
            "firstName VARCHAR, " +
            "lastName VARCHAR, " +
            "companyName VARCHAR, " +
            "address VARCHAR, " +
            "city VARCHAR, " +
            "county VARCHAR, " +
            "postal VARCHAR, " +
            "phone1 VARCHAR, " +
            "email VARCHAR) " +
            "TYPE IMap OPTIONS (" +
            "'keyFormat' = 'java'," +
            "'keyJavaClass' = 'java.lang.Integer'," +
            "'valueFormat' = 'compact'," +
            "'valueCompactTypeName' = 'hazelcast.microservices.common.Order')";



    public void doSQLMappings(HazelcastInstance hzClient){
        hzClient.getSql().execute(CUSTOMER_MAPPING_SQL);
        hzClient.getSql().execute(ORDER_MAPPING_SQL);
        hzClient.getSql().execute(ORDERS_WITH_CUSTOMERS_MAPPING_SQL);

        System.out.println("Initialized SQL Mappings");
    }
    public RefDataLoader() {
    }

    public void loadCustomerData(HazelcastInstance client) throws IOException {
        String str = "resources/uk-500.csv";
        Path file = Paths.get(str);
        //IMap<Integer, String > customerMap = client.getMap(CUSTOMER_MAP);
        IMap<Integer, Customer > customerMap = client.getMap(CUSTOMERS_MAP);

        Files.lines(file)
                .skip(1)
               .map(RefDataLoader::getCustomer)
               // .map(customer->getCustomer(customer))  // non method reference version of above
                .forEach(customer->customerMap.put(customer.getCustomerId(), customer));

        /* for (Map.Entry<Integer, Customer> entry : customerMap.entrySet()) {
            System.out.println(entry.getKey().toString() + " " + entry.getValue());
        } */
    }

    private static Customer getCustomer(String line) {
        String result = line.replaceAll("\"", "");
        String[] fields = result.split(",");
        //String[] fields = line.split(",");
       /*  return new Customer(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5],
                fields[6], fields[7], fields[8], fields[9], fields[10]);  */
        var customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID++);
        customer.setFirstName(fields[0]);
        customer.setLastName(fields[1]);
        customer.setCompanyName(fields[2]);
        customer.setAddress(fields[3]);
        customer.setCity(fields[4]);
        customer.setCounty(fields[5]);
        customer.setPostal(fields[6]);
        customer.setPhone1(fields[7]);
        customer.setPhone2(fields[8]);
        customer.setEmail(fields[9]);
        customer.setWeb(fields[10]);

        return customer;
    }

}
