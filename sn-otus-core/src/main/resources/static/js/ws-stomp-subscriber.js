
class WsStompSubscriber {

    constructor (wsURL, channel) {
        this.wsURL = wsURL;
        this.channel = channel;
    }

     subscribe(timeline) {
        stompClient = Stomp.client(this.wsURL);
        stompClient.connect({}, function(frame) {
            stompClient.subscribe(this.channel, function(msg) {
                timeline.store.push(data);
                timeline.updateTimeline();
            });
        });
    }

}