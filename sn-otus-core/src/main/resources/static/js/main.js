const API_URL = 'http://localhost:9081/tweets';
const WS_NOTIFICATION_SERVICE_URL = 'ws://localhost:9082/ws-otus-network';
const WS_CHANNEL = '/user/queue/tweet-feed-updates';

const http = new HTTP(API_URL);
const wsStomp = new WsStompSubscriber(WS_NOTIFICATION_SERVICE_URL, WS_CHANNEL);

http.fetchData().then(async (response) => {
    if(response.ok) {
        const data = await response.json();
        const timeline = new Timeline(data);
        wsStomp.subscribe(timeline);
    }
});