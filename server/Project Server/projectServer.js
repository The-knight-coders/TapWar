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
const games= {};

function get_connection_username(connection)
{
	for(var user_id in connections_map)
	{
		if(connections_map[user_id] == connection)     
		{
			return user_id;
		}
	}
	console.log("Not found username");
}

wsServer.on('request', (req) => {
    const connection = req.accept()
    console.log('new connection')

    // connections_map[username] = (connection)

    connection.on('message', (mes) => {

        console.log("message is ", mes.utf8Data.toString())

        var obj = JSON.parse(mes.utf8Data);
        if (obj.type == 'login') {
        	const username=obj['user_name'];
        	connections_map[username]=connection;
        	connection["player_username"] = username;
        	connection.sendUTF("login_success");
        }
        else if(obj.type=="create_game"){
            var game_id = uniqid();
            games[game_id] = {
            	"game_id": game_id,
            	"player_1": get_connection_username(connection),        
            	"player_2" : undefined
            };

            connection.sendUTF(game_id);
            console.log(games);
        }
        else if(obj.type=="join_game"){
        	const req_game_id=obj.game_id;

        	if(req_game_id in games){
        		games[req_game_id].player_2=get_connection_username(connection); 
        		console.log(games);

        		connection.sendUTF("yes");

        		connections_map[games[req_game_id].player_1].sendUTF("User_is_connected");
        	}else{
        		connection.sendUTF("no");
        	}

        } 
        else {
            console.log('No Request found');
        }

    })

    connection.on('close', (resCode, des) => {
        console.log('connection closed')
        // connections.splice(connections.indexOf(connection), 1)
    })
})

