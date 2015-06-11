var express = require('express');
// 调用 express 实例，它是一个函数，不带参数调用时，会返回一个 express 实例，将这个变量赋予 app 变量。
var app = express();
var superagent = require('superagent');
var cheerio = require('cheerio');
var utility = require('utility');
var mongooseObj = require('./mongoose').mongooseObj;
var mongoose = new mongooseObj();


var Cat = mongoose.db.model('Cat', {
    name: String,
    friends: [String],
    age: Number
});
var kitty = new Cat({ name: 'Zildjian', friends: ['tom', 'jerry']});
kitty.age = 3;



app.get('/', function (req, res, next) {
    // 用 superagent 去抓取 https://cnodejs.org/ 的内容
//    superagent.get('http://www.autohome.com.cn/car/?pvareaid=101452')
//        .end(function (err, sres) {
//            // 常规的错误处理
//            if (err) {
//                return next(err);
//            }
//            // sres.text 里面存储着网页的 html 内容，将它传给 cheerio.load 之后
//            // 就可以得到一个实现了 jquery 接口的变量，我们习惯性地将它命名为 `$`
//            // 剩下就都是 jquery 的内容了
//            var $ = cheerio.load(sres.text);
//            var items = [];
//
//            $('.find-letter-list li a').each(function (idx, element) {
//                var $element = $(element);
//                items.push({
//                    zimu: $element.html()
//                });
//            });
//
//
//
//
//        });

    mongoose.save(kitty,function(){
        console.log("保存成功");

        mongoose.findall(Cat,function(){
        },function(){
        });

    },function(err){

        console.log("保存失败");
    });

    res.send("");
});

app.get('/:a?/:b?/:c?', function (req,res) {
    res.send(req.params.a + ' ' + req.params.b + ' ' + req.params.c  + '   '+ req.query.q);
});

app.listen(3000, function () {
    console.log('app is listening at port 3000');
});