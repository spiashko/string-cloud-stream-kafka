package com.spaishko.stringcloudstreamkafka.producer.order.events;

import com.spaishko.stringcloudstreamkafka.producer.domainevent.EventType;
import com.spaishko.stringcloudstreamkafka.producer.order.OrderModel;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@ToString
@Getter
public class OrderCreated extends OrderEvent<OrderCreated.OrderCreatedDetails> {

    public OrderCreated(@NotNull OrderCreatedDetails details,
                        @NotNull UUID orderUuid) {
        super(details, orderUuid);
    }

    @Override
    public EventType getEventType() {
        return EventType.ORDER_CREATED;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderCreatedDetails {

        @NonNull
        private OrderModel createdOrder;

    }
}