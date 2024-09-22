package com.onkonfeton;

import com.onkonfeton.domain.Customer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String jsonString = """
            {
                "id": "3b049946-ed7e-40ba-a7cb-f3585409da22",
                "firstName": "Vitya",
                "lastName": "Ak",
                "dateBirth": "2003-11-03",
                "orders": [
                    {
                        "id": "956bb29b-8191-4de5-9e8e-8df759525831",
                        "products": [
                            {
                              "id": "50faf7eb-6792-45a7-a3cd-91bb63de48f6",
                              "name": "Теlеfон",
                              "price": 100.0
                            },
                            {
                              "id": "6b3a9d70-43e0-4c87-b72d-45fe79ee41c4",
                              "name": "Машина",
                              "price": 100.0
                            }
                        ],
                        "createDate": "2023-10-24T17:50:30.5470749+03:00"
                    }
                ]
            }
        """;


        JsonLib jsonLib = new JsonLib();
        Customer customer = jsonLib.deserialize(jsonString, Customer.class);
        System.out.println(customer.toString());

        System.out.println(jsonLib.serialize(customer));

    }
}