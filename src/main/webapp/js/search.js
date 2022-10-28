function search() {
    let textToSearch = document.getElementById("header_search_memory").value;
    let paragraph = document.getElementById("paragraph");
    console.log(textToSearch);

    //匹配字符
    textToSearch = textToSearch.replace(/[.*+?^${}()|[\][\\]/g, "\\$&");
    // g ：表示全局（global）模式，即模式将被应用于所有字符串，而非在发现第一个匹配项时立即停止；
    //i： 表示不区分大小写（case -insensitive） 模式， 即在确定匹配项时忽略模式与字符串的大小写；
    let pattern = new RegExp(`${textToSearch}`, "gi");
    paragraph.innerHTML = paragraph.textContent.replace(pattern, match => `<mark>${match}</mark>`);
}

//搜索功能的实现
ajax(`http://8.134.104.234:8080/ReciteMemory/modle/UserMemory?userId=${49}`, 'get', '', (str) => {
    let newstr = JSON.parse(str).msg;
    // console.log(newstr)
    console.log(newstr.data.userModle)

    let header_search_memory = document.getElementById("header_search_memory");
    let ul_context = $(".my_base .base_lis");
    console.log(ul_context.innerHTML)

    function createTemp() {
        //获取搜索框的值
        var search_value = header_search_memory.value;
        console.log(search_value);

        let arrnew = newstr.data.userModle.map((item, index) => {
            //Object.assign方法用来将源对象（source）的所有可枚举属性，复制到目标对象（target）。
            console.log(item.modleTitle);
            return Object.assign({}, {
                'modleTitle': item.modleTitle,
            })

        });
        //filter()方法用于过滤数组元素。该方法创建一个新数组, 其中包含通过所提供函数实现的测试的所有元素。filter()不会对空数组进行检测，也不会改变原始数组。
        var newData = arrnew.filter(item => {
            if (item.modleTitle.indexOf(search_value) > -1) { //indexOf方法中如果xxx.indexOf("")返回值为0
                console.log(item)
                return item;
            }
            return newData;
        })
        console.log(newData.length + "new")
        let content = "";
        if (newData.length > 0) {
            for (var i = 0; i < newData.length; i++) {
                for (let x of newstr.data.userModle) {
                    console.log(x.modleTitle)
                    console.log(newData[i].modleTitle)
                    if (x.modleTitle === newData[i].modleTitle) {
                        let newcon = x.content.replace(/<缩进>/g, '&nbsp;&nbsp;&nbsp;&nbsp;').replace(/<\/p>/g, '').replace(/<p>/g, '');
                        label = labelId2(x.modleLabel);
                        content += `
                    <li class="baseLis_fadeIn">
                        <div class="tp_inner">
                            <div class="modleId">${x.modleId}</div>
                            <div class="content">
                                <h3 class="title ellipsis">${x.modleTitle}</h3>
                                <div class="info ellipsis">${newcon}</div>
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
                            <div class="common">${x.common}</div>
                        </div>
                    </li>`
                    }
                }
            }
        }
        ul_context.innerHTML = content;
    }

    header_search_memory.onchange = () => {
        createTemp();
    }
}, true);

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

//新建模板到仓库
function newTP(title, context, modleId, label, common) {
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

//封装一个可以获取CSS选择器的函数
function $(selectors) {
    if (document.querySelectorAll(selectors).length != 1)
        return document.querySelectorAll(selectors);
    else
        return document.querySelector(selectors);
}