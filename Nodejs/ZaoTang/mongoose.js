// 首先引入 mongoose 这个模块
var Mongoose = require('mongoose');
Mongoose.connect('mongodb://127.0.0.1:27017/test');
//
////创建实体
//var Cat = Mongoose.model('Cat', {
//    name: String,
//    friends: [String],
//    age: Number
//});
////给实体赋值
//var kitty = new Cat({ name: 'ashe', friends: ['tom', 'jerry']});
//kitty.age = 3;
//
//kitty.save(function (err) {
//    if (err){
//        console.log('shibaishibai');
//
//    }else {
//        console.log('chenggong');
//    }
//});



var mongoose=function(){
   this.db=Mongoose;
}

//mongoose.db=Mongoose;

//save new object
mongoose.prototype.save = function(obj, success,error) {
// 调用 .save 方法后，mongoose 会去你的 mongodb 中的 test 数据库里，存入一条记录。
    obj.save(function (err) {
        if (err){
           error("数据插入失败");
        }else {
            success();
        }
    });

};

//save new object
mongoose.prototype.findall = function(obj, success,error) {
    obj.find({}, function (err, docs) {
         // docs 是查询的结果数组
            console.log(docs);
     });
};



exports.mongooseObj = mongoose;