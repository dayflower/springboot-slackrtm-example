# springboot-slackrtm-example

Example code of Slack RTM handling with Spring Boot framework.

The key point is that you need to implement periodical
[Ping and Pong](https://api.slack.com/rtm#real-time-messaging-rtm-api__ping-and-pong)
to make your application robust on websocket connection lost.
