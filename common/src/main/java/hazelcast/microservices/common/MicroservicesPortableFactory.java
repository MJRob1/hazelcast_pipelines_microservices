package hazelcast.microservices.common;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;

public class MicroservicesPortableFactory implements PortableFactory {

    public static final int ID = 1;
    @Override
    public Portable create(int classId) {
        if (Order.ID == classId)
            return new Order();
        else if (Customer.ID == classId)
            return new Customer();
        /* else if (StateStore.ID == classId)
            return new StateStore(); */
        else
            return null;
    }
}
