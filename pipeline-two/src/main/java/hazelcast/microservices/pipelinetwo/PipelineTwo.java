package hazelcast.microservices.pipelinetwo;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.*;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;

import java.util.Map;

public class PipelineTwo {

    private static final String PIPELINE_TWO_MAP = "orders-with-customers-map" ;
    public static Pipeline createPipeline(){
        Pipeline pipeline = Pipeline.create();
        StreamStage<Map.Entry<Integer, GenericRecord>> statusEvents = pipeline.readFrom(
                        Sources.<Integer, GenericRecord>mapJournal(PIPELINE_TWO_MAP,
                                JournalInitialPosition.START_FROM_CURRENT))
                .withTimestamps(item -> item.getValue().getInt64("eventTime"), 1000)
                .setName("machine status events");

        statusEvents.writeTo(Sinks.logger( event -> "Pipeline Two: New Event key = " + event.getKey() +
                ", New OrderId = " + event.getValue().getInt32("orderId") +
                ", New CustomerId = " + event.getValue().getInt32("customerId") +
                ", New Amount = " + event.getValue().getDecimal("amount") +
                ", New eventTime = " + event.getValue().getInt64("eventTime")));

        return pipeline;
    }

    public static void main(String []args){
        Pipeline pipeline = createPipeline();
        pipeline.setPreserveOrder(true);

        JobConfig jobConfig = new JobConfig();
        jobConfig.setName("Pipeline Two");
        HazelcastInstance hz = Hazelcast.bootstrappedInstance();
        hz.getJet().newJob(pipeline, jobConfig);
    }
}
