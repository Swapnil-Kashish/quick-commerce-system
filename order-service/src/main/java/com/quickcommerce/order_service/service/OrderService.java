package com.quickcommerce.order_service.service;

import com.quickcommerce.order_service.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    public Order createOrder(Order order) {

        String url = "http://localhost:8082/inventory/check?productId="
                + order.getProductId() + "&quantity=" + order.getQuantity();

        int attempts = 0;

        while (attempts < 3) {
            try {
                System.out.println("Calling inventory... attempt " + (attempts + 1));

                Boolean inStock = restTemplate.getForObject(url, Boolean.class);

                if (Boolean.FALSE.equals(inStock)) {
                    order.setStatus("FAILED");
                    return order;
                }

                order.setStatus("SUCCESS");
                return order;

            } catch (Exception e) {
                attempts++;
                System.out.println("Retry attempt: " + attempts);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        order.setStatus("FAILED");
        return order;
    }
}
