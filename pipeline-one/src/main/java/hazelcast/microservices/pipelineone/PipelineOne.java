package hazelcast.microservices.pipelineone;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.datamodel.Tuple2;
import com.hazelcast.jet.pipeline.*;
import com.hazelcast.map.IMap;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;

import java.util.Map;

public class PipelineOne {

    public static final String EVENT_MAP_NAME = "orders-map";
    private static final String CUSTOMERS_MAP = "customer-map" ;
    private static final String STATE_STORE_MAP = "state-store-map" ;
    private static final String ORDERS_WITH_CUSTOMERS_MAP = "orders-with-customers-map" ;


    public static Pipeline createPipeline(HazelcastInstance hz){
        IMap<Integer, GenericRecord> customerMap = hz.getMap(CUSTOMERS_MAP);
        IMap<Integer, GenericRecord> ordersWithCustomersMap = hz.getMap(ORDERS_WITH_CUSTOMERS_MAP);

        Pipeline pipeline = Pipeline.create();

        // Read Order events from source orders-map
        StreamStage<Map.Entry<Integer, GenericRecord>> statusEvents = pipeline.readFrom(
                        Sources.<Integer, GenericRecord>mapJournal(
                                EVENT_MAP_NAME,
                                JournalInitialPosition.START_FROM_OLDEST))
                .withTimestamps(item -> item.getValue().getInt64("eventTime"), 1000)
                .setName("machine status events");

        // Write to console to show have got new order values
        statusEvents.writeTo(Sinks.logger( event -> "New Event key = " + event.getKey() +
                        ", New OrderId = " + event.getValue().getInt32("orderId") +
                        ", New CustomerId = " + event.getValue().getInt32("customerId") +
                        ", New Amount = " + event.getValue().getDecimal("amount") +
                        ", New eventTime = " + event.getValue().getInt64("eventTime")));

        // Enrich order stream with information from customer map
        /* StreamStage<Tuple5<Integer, Integer, BigDecimal, String, String>> orderAndCustomer =
                statusEvents.mapUsingIMap(customerMap,
                order->order.getValue().getInt32("customerId"),
                (order, customer) ->Tuple5.tuple5(order.getValue().getInt32("orderId"),
                        order.getValue().getInt32("customerId"),
                        order.getValue().getDecimal("amount"),
                        customer.getString("firstName"),
                        customer.getString("lastName"))); */

         StreamStage<Tuple2<Integer, GenericRecord>> orderAndCustomer =
                statusEvents.mapUsingIMap(customerMap,
                order->order.getValue().getInt32("customerId"),
                (order, customer) -> updateOrder(order, customer));

        orderAndCustomer.writeTo(Sinks.logger(event -> "OrderId = " + event.f0() +
                " Modified Order = " + event.f1()));

        orderAndCustomer.writeTo(Sinks.map(ordersWithCustomersMap));

      /* StreamStage<Tuple2<Integer, String>> orderAndCustomer =
                statusEvents.mapUsingIMap(customerMap,
                        order->order.getValue().getInt32("customerId"),
                        (order, customer) ->Tuple2.tuple2(order.getValue().getInt32("orderId"),
                                ((customer.getInt32("customerId")) + "," +
                                        customer.getString("firstName") + "," +
                                        customer.getString("lastName") + "," +
                                        customer.getString("companyName") + "," +
                                        customer.getString("address") + "," +
                                        customer.getString("city") + "," +
                                        customer.getString("county") + "," +
                                        customer.getString("postal") + "," +
                                        customer.getString("phone1") + "," +
                                        customer.getString("phone2") + "," +
                                        customer.getString("email") + "," +
                                        customer.getString("web"))));

        orderAndCustomer.writeTo(Sinks.logger(event -> "OrderId = " + event.f0() +
                        " CustomerString = " + event.f1()));

        orderAndCustomer.writeTo(Sinks.map(stateStoreMap)); */

        /*
        // Test can pick selected values from Tuple5. Write selected values to console
        orderAndCustomer.writeTo(Sinks.logger(event -> "OrderId = " + event.f0() +
                                " CustomerId = " + event.f1() +
                                " amount = " + event.f2() +
                                " firstName = " + event.f3() +
                                " lastName = " + event.f4()));

        // Store state needed for other microservices in state-store map


        // Sink to pipelineTwo source map
        orderAndCustomer
                .map(event -> Tuple2.tuple2(event.f0(), event.f3()))
                //.writeTo(Sinks.logger(event -> "********* JOURNAL MAP   OrderId = " + event.f0() +
                //        " firstName = " + event.f1()));
                .writeTo(Sinks.map(pipelineTwoMap));  */

        return pipeline;
    }

    private static Tuple2<Integer, GenericRecord> updateOrder(Map.Entry<Integer, GenericRecord> order, GenericRecord customer) {

        GenericRecord modifiedGenericRecord = order.getValue().newBuilderWithClone()
                .setString("firstName", customer.getString("firstName"))
                .setString("lastName", customer.getString("lastName"))
                .setString("companyName", customer.getString("companyName"))
                .setString("address", customer.getString("address"))
                .setString("city", customer.getString("city"))
                .setString("county", customer.getString("county"))
                .setString("postal", customer.getString("postal"))
                .setString("phone1", customer.getString("phone1"))
                .setString("email", customer.getString("email"))
                .build();

        return Tuple2.tuple2(order.getKey(), modifiedGenericRecord);
    }


    public static void main(String []args){
        HazelcastInstance hz = Hazelcast.bootstrappedInstance();

        Pipeline pipeline = createPipeline(hz);
        pipeline.setPreserveOrder(true);

        JobConfig jobConfig = new JobConfig();
        jobConfig.setName("Pipeline One");

        hz.getJet().newJob(pipeline, jobConfig);
    }
}
