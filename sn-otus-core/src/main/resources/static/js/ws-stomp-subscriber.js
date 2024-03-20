
class WsStompSubscriber {

    constructor (wsURL) {
        this.wsURL = wsURL;
    }

     subscribe(timeline, channel) {
        var stompClient = Stomp.client(this.wsURL);
        stompClient.connect({}, function(frame) {
            stompClient.subscribe(channel, function(msg) {
                var data = JSON.parse(msg.body);
                timeline.store.push(data);
                timeline.updateTimeline();
            });
        });
    }

}