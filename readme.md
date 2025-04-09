# Messaging App
## Version 0.3

## Feature
* Register and Login New User (Only possible with OAuth2 GitHub login)
* Login with pre-saved Credentials in DB. 
* Add and save Contacts by Username (Check if User exists in DB added)
* delete Contacts with username
* Messaging to other Users by username
* Messages will be saved locally and in server Database as temporal Data, if the client is Offline.

## Extra

## Annotations
### Request JSON to Login.
* Payload/Body in JSON Get Request to http://localhost:8080/api/auth/login:
```
{
    "username": "testuser1",
    "password": "testpassword1"
}   
```
* Response JWT with token in Payload:
```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoidGVzdHVzZXIiLCJpc3MiOiJtZXNzYWdpbmctYXBwLW1hcmZlciIsImlhdCI6MTc0NDA1MzIwMCwiZXhwIjoxNzQ0MTM5NjAwfQ.gMnRPpdmuDvOftVCna0JI5KRFh78xgDnIf_oeHEo6GA"
}
```
### Request OAuth2 Github
Get Request to http://localhost:8080/oauth2/authorization/github  
After Successfully login in GitHub JWT response:
```
{"token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZmNwLWh1bmFuY2UiLCJpc3MiOiJtZXNzYWdpbmctYXBwLW1hcmZlciIsImlhdCI6MTc0NDA5OTc1NywiZXhwIjoxNzQ0MTg2MTU3fQ.xK_1xHXqdym8CIobrymPuAFLhlukK60J_32aaRatI2I",
"userId": "27f279b4-13e1-4e04-8b16-d301e8be0cb9"}
```
### Send Message in JWT format
Post request to http://localhost:8080/message/send  
In Header Authorization-Type: Bearer Token  
In Body Recipient's Username and message.
```
{
    "username": "user2",
    "content": "Welcome! This is a test." 
}
```
#### Server's responses
* Message delivered
```
{
    "content": "Welcome! This is a test.",
    "messageId": "e5571de8-c129-4693-af61-b8e018699d65",
    "recipient": "user2",
    "timestamp": "2025-04-08T13:45:16.3691284"
}
```
Exceptions:
* Recipient invalid
```
{
    "status": 400,
    "error": "Recipient not found!"
}
```
* Sender and Recipient the same user
```
{
    "status": 400,
    "error": "Cannot send message to yourself"
}
```
### Get undelivered Messages for Client
Get Request to http://localhost:8080/message/undelivered  
In Header Authorization-Type: Bearer Token  
Body empty  
Server's Response (Recipient ID will be removed, not needed in Client)
```
[
    {
        "recipient": "user2",
        "id": "e5571de8-c129-4693-af61-b8e018699d65",
        "content": "Welcome! This is a test.",
        "timestamp": "2025-04-08T13:45:16.369128"
    },
    {
        "recipientId": "user2",
        "id": "e5571de8-c129-4693-af61-b8e018699d65",
        "content": "Another message.",
        "timestamp": "2025-04-08T13:45:16.369128"
    }
]
```
### Request to consult if an User exits in the DB
Get Request to http://localhost:8080/api/users/find 
In Header Authorization-Type: Bearer Token  
Body: username to check
```
{
    "username": "user2"
}
```
Server's JSON Response 200 -> Exists / 400 -> Not found.
```
{
    "message": "User not found",
    "status": 404
}

{
    "message": "User exists in DB",
    "status": 200
}
```