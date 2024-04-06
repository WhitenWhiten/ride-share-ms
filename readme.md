返回所有Passenger	http://localhost:8080/ps/all-passengers

返回所有Driver	http://localhost:8080/ds/drivers

返回所有Order	http://localhost:8080/os/all-orders



乘客检索指定地点的司机：   http://localhost:8080/ps/search-location?location=%E6%AD%A6%E6%B1%89

乘客检索指定类型的司机： http://localhost:8080/ps/search-type?type=fast

乘客充值：Put方法(见apifox) http://localhost:8080/ps/add-balance?passengerId=1&val=1000

乘客下单：Post方法 http://localhost:8080/ps/make-order?passenger_id=1&driver_id=1&price=100

查看单一客户所有订单（通过passenger service访问）：http://localhost:8080/ps/passenger-orders?passengerId=1

查看所有订单（通过order service访问）： http://localhost:8080/os/all-orders

查看单一客户所有订单：http://localhost:8080/ps/



查看某一订单状态：http://localhost:8080/os/order-state?orderId=1



添加新司机：Post方法 http://127.0.0.1:8080/ds/new-driver?name=%E5%8F%B8%E6%9C%BAx&vehicleType=%E6%96%B0%E8%83%BD%E6%BA%90%E6%B1%BD%E8%BD%A6

司机接单：Put方法 http://localhost:8080/ds/accept-order?driverId=1&orderId=1

司机查看自己余额：http://localhost:8080/ds/driver-balance?id=1

司机查看当前等待自己接单的订单编号: http://localhost:8080/ds/waiting-orders?id=1

司机更新自己的位置：Put方法 http://localhost:8080/ds/update-location?id=1&newLocation=纽约



乘客确定服务已完成、关闭订单: Put方法 http://localhost:8080/ps/close-order?orderId=1

（完成对乘客余额的检查、order service标记订单状态为closed，driver service更新司机余额）



