# ecommerce-app

This microservice exposes the apis for various operations of order-service like creation, retrieval and cancellation.
It saves all the information regarding the order like Addresses involved, payment and shiiping information and delegates payment to payment module.




It is hosted as an individual microservice and works with a eureka server, but the apis for both this module and batch microservice as exposed via api-gateway module hosted on 8080. All the endpoints could be accessed via swagger.
