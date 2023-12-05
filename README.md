
Generate token 
```curl
% curl -d "client_id=rest_api&username=lumberjack_programmer&password=password&grant_type=password" \
-H "Content-Type: application/x-www-form-urlencoded" \
-X POST http://localhost:8080/realms/SpringBootDemo/protocol/openid-connect/token
```
```
http://localhost:8080/realms/SpringBootDemo/.well-known/openid-configuration
```