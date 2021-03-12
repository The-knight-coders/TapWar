const SocketServer = require('websocket').server
const http = require('http')
const uniqid = require("uniqid")


const server = http.createServer((req, res) => {
    res.writeHead(200, { 'Content-Type': 'text/plain' });
    // Send back a response and end the connection
    res.end('Hello User!\n');

})
var port = process.env.PORT || 3000;
server.listen(port, () => {
    console.log("Listening on port " + port + "...")
})

wsServer = new SocketServer({ httpServer: server })
const connections_map = {};
const games = {};

function get_connection_username(connection) {
    for (var user_id in connections_map) {
        if (connections_map[user_id] == connection) {
            return user_id;
        }
    }
    console.log("Not found username");
}



wsServer.on('request', (req) => {
    const connection = req.accept()
    console.log('new connection')

    // connections_map[username] = (connection)
    let message = {
        "type" : undefined,
        "game" : undefined,
        "status" : undefined
    
    }
   

    connection.on('message', (mes) => {

        console.log("message is ", mes.utf8Data.toString())

        var obj = JSON.parse(mes.utf8Data);
        if (obj.type == 'login') {
            const username = obj['user_name'];
            connections_map[username] = connection;
            connection["player_username"] = username;

            message.type = "Login";
            message.status = "Successfull";
            connection.sendUTF(JSON.stringify(message));
            console.log("login successfull : " + username);
        } else if (obj.type == "create_game") {
            var game_id = uniqid();
            games[game_id] = {
                "game_id": game_id,
                "player_1": get_connection_username(connection),
                "player_2": undefined
            };

            message.type = "Create room";
            message.game = games[game_id];
            message.status = "Pending";

            connection.sendUTF(JSON.stringify(message));
            console.log(games);
            console.log(message);
        } else if (obj.type == "join_game") {
            const req_game_id = obj.game_id;
            message.type = "Join room";
            if (req_game_id in games) {
                games[req_game_id].player_2 = get_connection_username(connection);
                console.log(games);
                message.game = games[req_game_id];
                message.status = "Successful";
                connection.sendUTF(JSON.stringify(message));
                console.log("room joined");
                console.log(message);
                connections_map[games[req_game_id].player_1].sendUTF(JSON.stringify(message));
            } else {
               
                message.status = "wrong room code";
                connection.sendUTF(JSON.stringify(message));
                console.log('Room does not exists');
            }

        } else if(obj.type == "cancel_game"){
            const req_game_id = obj.game_id;
            delete games[req_game_id];
            console.log("rooms been deleted : " + req_game_id);
            message.type = "Cancel room";
            message.game = undefined;
            message.status = "canceled";
            console.log('No room found');
            connection.sendUTF(JSON.stringify(message));
            console.log(games);
        }

    })

    connection.on('close', (resCode, des) => {
        console.log('connection closed')
            // connections.splice(connections.indexOf(connection))
    })
})