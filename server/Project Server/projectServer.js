const http = require('http');
const SocketServer = require('websocket').server;
const hostname = 'localhost'
wsServer = new SocketServer({httpServer:server});

server.listen(3000,hostname,()=>{
    console.log(`Server running at http://${hostname}:${3000}`)
})

const connections = [];