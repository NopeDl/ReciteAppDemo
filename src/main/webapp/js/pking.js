let ws = new WebSocket(`ws://8.134.04.234:8080//ReciteMemory/PK/${curr.userId}/${139}/${1}`);

console.log(ws.readyState);

ws.onopen = function() {
    console.log('连接成功...');
    window.onkeypress = function(e) {
        console.log(1);
        if (e.keyCode === 13) {
            ws.send("START");
        }
    }
}

ws.onerror = function() {
    console.log('连接失败...')
    reConnect();
}

ws.onmessage = (event) => {
    if (event.data) {
        let res = JSON.parse(event.data)
        console.log(res);
    }
}

ws.onclose = function(event) {
    console.log('连接已关闭...')
    console.log('websocket 断开: ' + event.code + ' ' + event.reason + ' ' + event.wasClean)
}