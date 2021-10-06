// Source: https://www.npmjs.com/package/node-rtsp-stream
//L
Stream = require('node-rtsp-stream')
var WebSocketServer = require('ws')

stream = new Stream({
  name: 'name',
  streamUrl: 'rtsp://root:ipcam@192.168.1.22/live.sdp',  //rtsp of the IPcamera
  wsPort: 9999,

  // to convert video format
  
  ffmpegOptions: { // options ffmpeg flags
    '-stats': '', // an option with no neccessary value uses a blank string
    '-r': 30 // options with required values specify the value after the key
  }
})
