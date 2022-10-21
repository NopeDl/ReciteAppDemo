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
function ajax(url, method, req, funC, flag) {
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
    if (flag)
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
function saveData(name, data) {
    localStorage.setItem(name, JSON.stringify(data));
}


//为数组对象添加自定义方法remove,可通过元素的值查找元素并删除
Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

//新建模板到仓库
function newTP(title, context, modleId, label,common, flag) {
    let li = document.createElement('li');
    li.innerHTML = `<div class="tp_inner">
                        <div class="modleId">${modleId}</div>
                        <div class="content">
                            <h3 class="title ellipsis">${title}</h3>
                            <div class="info ellipsis">${context}</div>
                        </div>
                        <div class="tip">
                            <div class="date">2022-10-15</div>
                            <div class="label">
                                <span class="iconfont icon-shuqianguanli"></span>
                                <span>${label}</span>
                            </div>
                        </div>
                        <div class="template_btn">
                            <div class="tp_btn edit">编辑</div>
                            <div class="tp_btn del">删除</div>
                        </div>
                        <div class="common">${common}</div>
                    </div>`
    if (flag) {
        $('.my_base .base_lis').prepend(li);
    } else {
        $('.collection_base .base_lis').prepend(li);
    }


}

//社区渲染
function comTP(title, context, modleId, label, username,name_flag) {
    let interactive = '';
    if(name_flag){
        interactive = `<span class="dainzan  iconfont icon-shoucang">&nbsp;&nbsp; <i>收藏</i></span>
                        <span class="jifen iconfont icon-jifenhuiyuan"> &nbsp;<i>打赏</i></span>`
    }else{
        interactive = `<span class="dainzan  iconfont icon-shoucang hidden">&nbsp;&nbsp; <i>收藏</i></span>
                        <span class="jifen iconfont icon-jifenhuiyuan hidden"> &nbsp;<i>打赏</i></span>
                        <span class="shanchu iconfont icon-a-shanchulajitong"> &nbsp;<i>删除</i></span>
                        `
    }
    let li = document.createElement('li');
    li.innerHTML = `<div class="modleId">${modleId}</div>
                        <div class="color">
                            <div class="upper">
                                <div class="head_portrait">
                                    <img src="./images/头像/头像-女学生2.png" alt="">
                                </div>
                                <div class="idname1">${username}</div>
                            </div>

                            <div class="content">
                                <h4 class="title ellipsis">${title}</h4>
                                <div class="info_box">
                                    <div class="info ellipsis">${context}</div>
                                </div>
                            </div>
                            <div class="click">
                                <div class="label">
                                    <span class="iconfont icon-shuqianguanli"></span>
                                    <span>${labelId2(label)}</span>
                                </div>
                                <div class="inter_box" id="interactive">
                                    <div class="interactive">
                                        ${interactive}
                                    </div>
                                </div>
                                <i class=" menu iconfont icon-shixincaidan"></i>
                            </div>
                        </div>`

    $('.community_ul').prepend(li);
}

//上传页面渲染模板
function UploadTP(title, context, modleId, label,common) {
    let select = '';
    if(common == 1){
        select = `<div class="circle selected">
                    <i class="iconfont icon-xuanze1"></i>
                </div>`
    }else{
        select = `<div class="circle">
                    <i class="iconfont icon-xuanze1"></i>
                </div>`
    }
    let li = document.createElement('li');
    li.innerHTML = `<div class="modleId">${modleId}</div>
                    <div class="content">
                        <div class="title_label">
                            <h4 class="title ellipsis">${title}</h4>
                            <div class="label">${label}</div>
                        </div>
                        <div class="info_box">
                            <div class="info ellipsis">${context}</div>
                        </div>
                    </div>
                    <div class="select">${select}</div>`
    $('.upload_page ul').prepend(li);
}





//转换标签成id
function labelId1(str) {
    if (str == '教资')
        return 1;
    if (str == '考研')
        return 2;
    if (str == '通识课')
        return 3;
}

//转换id成标签
function labelId2(num) {
    if (num == 1)
        return '教资';
    if (num == 2)
        return '考研';
    if (num == 3)
        return '通识课';
}