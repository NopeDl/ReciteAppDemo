//封装一个可以获取CSS选择器的函数
function $(selectors) {
    if (document.querySelectorAll(selectors).length != 1)
        return document.querySelectorAll(selectors);
    else
        return document.querySelector(selectors);
}

//封装一个返回数组的函数
function all(selectors) {
    return document.querySelectorAll(selectors);
}

//封装一个发送ajax请求和处理数据的的函数
function ajax(url, method, req, funC,flag) {
    //1.创建Ajax对象
    var xhr = null;
    if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest();
    } else {
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    }
    //2.连接服务器
    xhr.open(method, url);
    //3.设置请求头
    if(flag)
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    //4.发送请求
    xhr.send(req);
    //5.接收服务器的返回
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status >= 200 && xhr.status < 300) {
                funC(xhr.responseText);
            }
        }
    }
}

//读取本地存储数据
function getData(name) {
    let data = localStorage.getItem(name);
    if (data !== null) {
        //把字符串转换成对象
        return JSON.parse(data);
    } else {
        return [];
    }
}

//保存本地存储数据
function saveData(name,data) {
    localStorage.setItem(name, JSON.stringify(data));
}


//为数组对象添加自定义方法remove,可通过元素的值查找元素并删除
Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};