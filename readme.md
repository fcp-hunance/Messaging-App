# Messaging App
## Version 0.2


## Feature
* Register and Login New User (Server prove to unique Username)
* Add and save Contacts by Username
* delete Contacts with id (id will be fetch after successfully added from Contact)
* Messaging to other Users by username.
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
//TODO How implement SessionID in desktopApp  
In Body Recipient's Username and message.
```
{
    "username": "user2",
    "content": "Welcome! This is a test." 
}
```
Server's response (Recipient's ID will be removed, not needed in client)
```
{
    "content": "Welcome! This is a test.",
    "messageId": "e5571de8-c129-4693-af61-b8e018699d65",
    "recipientId": "a016961e-db33-49ec-b010-b4cc99f1999f",
    "timestamp": "2025-04-08T13:45:16.3691284"
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
        "recipientId": "a016961e-db33-49ec-b010-b4cc99f1999f",
        "id": "e5571de8-c129-4693-af61-b8e018699d65",
        "content": "Welcome! This is a test.",
        "timestamp": "2025-04-08T13:45:16.369128"
    },
    {
        "recipientId": "a016961e-db33-49ec-b010-b4cc99f1999f",
        "id": "e5571de8-c129-4693-af61-b8e018699d65",
        "content": "Another message.",
        "timestamp": "2025-04-08T13:45:16.369128"
    }
]
```

