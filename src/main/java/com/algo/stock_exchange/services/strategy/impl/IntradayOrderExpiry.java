package com.algo.stock_exchange.services.strategy.impl;

import com.algo.stock_exchange.models.Order;
import com.algo.stock_exchange.models.OrderStatus;
import com.algo.stock_exchange.services.strategy.OrderExpiryStrategy;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class IntradayOrderExpiry implements OrderExpiryStrategy {

    /* While you are currently using java.util.Date,
        it is highly recommended in modern enterprise Java (Java 8+) to use the java.time API.
         java.util.Date is notoriously difficult to work with and isn't thread-safe.

        I have provided the modern best-practice approach first, followed by the
        legacy approach in case you are working with an older codebase that restricts
        you to java.util.Date.
    */

    @Override
    public Boolean checkOrderExpiry(Order order) {
        // All intraday orders will get closed by 3.15 PM

        Date orderAcceptedTime = order.getCreatedTimestamp();
        Date currentTime = new Date(System.currentTimeMillis());

        if (OrderStatus.PENDING.toString().equalsIgnoreCase(String.valueOf(order.getOrderStatus()))) {
            return false;
        }

        // 2. Convert legacy java.util.Date to modern java.time.LocalDateTime
        LocalDateTime orderTime = orderAcceptedTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime now = currentTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // 3. Create the 3:15 PM deadline for the SAME DAY the order was created
        LocalDateTime deadline = orderTime.with(LocalTime.of(15, 15));

        order.setOrderStatus(OrderStatus.CANCELLED);

        // 4. Return true if the current time has passed the deadline
        return now.isAfter(deadline);
    }


}
