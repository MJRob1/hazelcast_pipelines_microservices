package hazelcast.microservices.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import hazelcast.microservices.common.MicroservicesPortableFactory;
import hazelcast.microservices.common.Order;

import static java.lang.Thread.sleep;

public class Client {

    private static final String ORDERS_MAP = "orders-map" ;

    public static void main(String[] args) throws Exception {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");
        clientConfig.getNetworkConfig().addAddress("127.0.0.1");
        clientConfig.getSerializationConfig().getPortableFactories()
                .put(MicroservicesPortableFactory.ID, new MicroservicesPortableFactory());

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        RefDataLoader dataLoader = new RefDataLoader();
        dataLoader.doSQLMappings(client);
        dataLoader.loadCustomerData(client);

       // Map<Integer, Order> batch = new HashMap<>();
        IMap<Integer, Order> ordersMap = client.getMap(ORDERS_MAP);


        for (int i = 1; i < 10; i++) {

            Order order = Order.fake(i);

           /* batch.put(order.getOrderId(), order);
            ordersMap.putAll(batch);
            batch.clear(); */

            ordersMap.put(order.getOrderId(), order);
            sleep(5000);
        }

       /* for (Map.Entry<Integer, Order> entry : ordersMap.entrySet()) {
           // System.out.println(entry.getKey() + " " + entry.getValue().getOrderId().toString());
            System.out.println(entry.getKey() + " " + entry.getValue());
        } */

        HazelcastClient.shutdownAll();
    }


}
