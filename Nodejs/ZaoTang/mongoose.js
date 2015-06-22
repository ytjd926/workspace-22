// 首先引入 mongoose 这个模块
var Mongoose = require('mongoose');
Mongoose.connect('mongodb://127.0.0.1:27017/ZaoTang');

//创建实体
var Cat = Mongoose.model('Cat', {
    name: String,
    friends: [String],
    age: Number
});
//记录单间状态
var privateRoomModel=Mongoose.model('RoomList', {
    roomName:String,//房间号：105
    floor:String,//1 ：1楼，2：2楼
    isSingle:String,//2双缸，1单缸
    status:String,  //0未使用，1使用中
    usedTime:String //使用时间
});
//收入
var incomeModel = Mongoose.model('incomeLog', {
    money:Number,//收入
    type:Number,//收入类型 0：大池 大人， 1：大池 小孩     2：饮料    3：其他    4：单间
    time:Number,//收入时间
    remark:String //备注
});
//字典表
var dictModel = Mongoose.model('Dict', {
    name: String,
    value: String
});




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
    this.privateRoomModel = privateRoomModel;
    this.dictModel = dictModel;
    this.incomeModel = incomeModel;
}

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
mongoose.prototype.findAll = function(obj, success,error) {
    obj.find({},
        function (err, docs) {
        if (err){
            error(err);
        }else {
            success(docs);
        }

    });
};







exports.mongooseObj = mongoose;