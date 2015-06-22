//1  
var http = require('http'),
    express = require('express'),
    path = require('path');
var mongooseObj = require('./mongoose').mongooseObj;
var mongoose = new mongooseObj();

var exec = require('child_process').exec;





//2  
var app = express();
app.set('port', process.env.PORT || 3000);
app.use(express.static(path.join(__dirname, '')));

//app.get('/', function (req, res) {
//    res.send('<html><body><h1>Hello World</h1></body></html>');
//});


//app.get('/:a?/:b?/:c?', function (req,res) {
//    //res.send(req.params.a + ' ' + req.params.b + ' ' + req.params.c);
//    console.log("3333333");
////    res.redirect('http://127.0.0.1:3000/public/main/hello.html');//重定向
//});



//——————————————————单间相关
//新增房间
app.get('/saveRoom',function(req,res){
    var roomCode=req.query.roomCode;
    var isSingle=req.query.isSingle;
    var floor = req.query.floor;
    var status="0";
//    var kitty = new Cat({ name: 'Zildjian', friends: ['tom', 'jerry']});
//    kitty.age = 3;
    var room=new mongoose.privateRoomModel();
    room.roomName = roomCode;
    room.isSingle=isSingle;
    room.floor = floor;
    room.status = status;

    mongoose.save(room,function(){
        console.log("保存成功");
        res.send("1");
    },function(err){
        res.send("0");
        console.log("保存失败");
    });
});
app.get("/findAllRoom",function(req,res){
    mongoose.findAll(mongoose.privateRoomModel,function(doct){
    //SUCCESS
      res.send(doct);
    },function(err){
    //FAIL
        res.send("");
    });

});
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
app.get("/updateRoom",function(req,res){
    var roomCode=req.query.roomCode;
    var conditions = {roomName : roomCode};

    var update     = {$set : {status : 1, usedTime : new Date().Format("yyyy-MM-dd hh:mm:ss")}};
    var options    = {upsert : true};
    mongoose.privateRoomModel.update(conditions, update, options, function(error){
        if(error) {
            console.log(error);
            res.send(error);
        } else {
            console.log('update ok!');
            res.send("update OK!");
        }
    });

});
//————————————————————————单间相关 完毕


//————————————————————————收入相关
app.get("/saveIncome",function(req,res){
    var income=new mongoose.incomeModel();
    income.money =req.query.money;
    income.type = req.query.type;
    income.time = new Date().getTime();
    income.remark = req.query.remark;

    mongoose.save(income,function(){
        console.log("保存成功");
    },function(err){
        console.log("保存失败");
    });

});


//————————————————————————收入相关  完毕


//————————————————————————字典相关
app.get("/saveDict",function(req,res){
    var name = req.query.name;
    var value = req.query.value;

    var conditions = {name : name};

    var update     = {$set : {value : value}};
    var options    = {upsert : true};
    mongoose.dictModel.update(conditions, update, options, function(error){
        if(error) {
            console.log(error);
            res.send(error);
        } else {
            console.log('update ok!');
            res.send("update OK!");
        }
    });
});

app.get("/getDict",function(req,res){
    var name = req.query.name;
    mongoose.dictModel.findOne({ name: name}, function (err, doc){
        // doc 是单个文档
        res.send(doc);

    });


});


//————————————————————————字典相关  完毕



app.use(function (req,res) { //1
    res.render('404', {url:req.url}); //2
});

http.createServer(app).listen(app.get('port'), function(){
    console.log('Express server listening on port ' + app.get('port'));
   var last =  exec('start "" "http://127.0.0.1:3000/public/main/AddInPrivate.html"');
         last.on('exit', function (code) {
    });
});