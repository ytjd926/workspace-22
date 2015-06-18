//1  
var http = require('http'),
    express = require('express'),
    path = require('path');
var exec = require('child_process').exec;





//2  
var app = express();
app.set('port', process.env.PORT || 3000);
app.use(express.static(path.join(__dirname, '')));

//app.get('/', function (req, res) {
//    res.send('<html><body><h1>Hello World</h1></body></html>');
//});


app.get('/:a?/:b?/:c?', function (req,res) {
    //res.send(req.params.a + ' ' + req.params.b + ' ' + req.params.c);
    console.log("3333333");
    res.redirect('http://127.0.0.1:3000/public/main/hello.html');//重定向
});

app.use(function (req,res) { //1
    res.render('404', {url:req.url}); //2
});

http.createServer(app).listen(app.get('port'), function(){
    console.log('Express server listening on port ' + app.get('port'));
   var last =  exec('start "" "http://127.0.0.1:3000/public/main/AddInPrivate.html"');
    last.on('exit', function (code) {
    });
});