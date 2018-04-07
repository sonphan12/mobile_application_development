# mobile_application_development

## Ex1/Part 1:
- Eclipse Oxygen project
- Ứng dụng lấy địa chỉ từ latitude và longitude.
- Trên nền tảng java swing
- Dùng web service geocoding của google
- File .jar để chạy ứng dụng nằm trong Ex1/Part 1/app
## Ex1/Part 2:
- Eclipse Oxygen project
- RESTful web service tính khoảng cách giữa 2 điểm với kinh độ và vĩ độ của 2 điểm đó
- Sử dụng Jersey Framework
- File .war để deploy web service nằm trong Ex2/Part 2/war
- Đã deploy thành công với tomcat 9.0.6
###### Hướng dẫn dùng webservice này:
- URL: http://localhost:8080/Part2/rest-points/app/[lat1]@[lon1],[lat2]@[lon2]
- Ví dụ khi cần tính khoảng cách giữa 2 điểm có latitude và longitude lần lượt là ```[10.850700, 106.764722]``` và ```[11.582430, 108.991113]``` thì dùng URL: http://localhost:8080/Part2/rest-points/app/10.850700@106.764722,11.582430@108.991113
- Web service sẽ trả về kết quả ở dạng json là: ```{ "distance":256.0 }```
## Ex2:
- App chuyển đổi tiền tệ
- Tỉ lệ tiền tệ được cập nhật mới nhất, dùng webservice của https://fixer.io/
- Dùng Volley của android để request webservice
- Có dữ liệu backup từ lần request webservice gần nhất đề phòng trường hợp dùng offline.
- File apk nằm trong thư mục:```Ex2\app\apk``` 
