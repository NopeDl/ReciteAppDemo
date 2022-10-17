//上传文件
let curr = getData('current_user');
let newtitle = null;
let newcontext = null;
var newTPFlag = false;


//获取用户模板
ajax(`http://8.134.104.234:8080/ReciteMemory/modle/UserMemory?userId=${curr.userId}`, 'get', '', (str) => {
    let newstr = JSON.parse(str).msg;
    if (newstr.data.userModle) {
        let tparr = newstr.data.userModle;
        for (let x of tparr) {
            newTP(x.modleTitle, x.content, x.modleId);
        }
    } else {
        //刷新仓库
        $('.footer_nav li')[0].onclick();
    }
}, true);
//获取用户信息
ajax(`http://8.134.104.234:8080/ReciteMemory/user.do/UserMsg?userId=${curr.userId}`, 'get', '', (str) => {
    let newstr = JSON.parse(str).msg;
    let userInfo = newstr.data.user;
    curr['userInfo'] = userInfo;
    console.log(curr);
    saveData('current_user', curr);
    //个人页面渲染
    for (let x of $('.idname'))
        x.innerHTML = curr.userInfo.nickName;
    $('.personal_box .phone').innerHTML = curr.phone;
})


//上传文件
$('.Making_page .header_left input').onchange = function (e) {
    let file = e.target.files[0];
    console.log(e.target.files);
    if (e.target.files.length != 0) {
        $('.Making_page .loading').style.display = 'block';
        let fd = new FormData($('.upload_form'));
        ajax(`http://8.134.104.234:8080/ReciteMemory/upload/parseContent?userId=${curr.userId}`, 'post', fd, (str) => {
            $('.Making_page .loading').style.display = 'none';
            let newstr = JSON.parse(str).msg;
            console.log(newstr);
            let context = newstr.data.context;
            //将文件内容渲染到页面
            $('.Making_page .title input').value = file.name;
            $('.Making_page .text_box').innerHTML = context;
        }, false)
    }
}
//点击出现弹窗
$('.Making_page .header_right').onclick = () => {
    //标题和文本内容不能为空
    if ($('.Making_page .title input').value == '' || $('.Making_page .text_box').innerHTML == '') {
        $('.Making_page .popup2 .popup_box').innerHTML = '标题和文本内容不能为空';
        $('.Making_page .popup2').style.display = 'block';
        return;
    }

    // 标题一致就取消保存并提醒
    for (let x of all('.my_base .title')) {
        if (x.innerHTML == $('.Making_page .title input').value) {
            $('.Making_page .popup2 .popup_box').innerHTML = '标题不能与记忆库的模板重复';
            $('.Making_page .popup2').style.display = 'block';
            return;
        }
    }
    $('.Making_page .popup').style.display = 'block';
    newtitle = $('.Making_page .title input').value;
    newcontext = $('.Making_page .text_box').innerHTML;

}
//点击关闭弹窗
$('.Making_page .popup').onclick = () => $('.Making_page .popup').style.display = 'none';
$('.Making_page .popup2').onclick = () => $('.Making_page .popup2').style.display = 'none';
//阻止事件冒泡
$('.Making_page .popup .popup_box').onclick = (e) => e.stopPropagation();
$('.Making_page .popup2 .popup_box').onclick = (e) => e.stopPropagation();

//点击保存文件
$('.Making_page .popup_box button')[0].onclick = () => {
    // 如果标题和内容不为空
    if (newtitle != '' && newcontext != '') {
        //创建模板
        console.log(newcontext);
        let poststr = `context=${newcontext}&userId=${curr.userId}&modleTitle=${newtitle}&overWrite=0&modleLabel=1`
        ajax(`http://8.134.104.234:8080/ReciteMemory/modle/MakeModle`, 'post', poststr, (str) => {
            let newstr = JSON.parse(str).msg;
            let modle = newstr.data.modle;
            newTP(newtitle, newcontext, modle.modleId);
            //清空编辑内容
            $('.Making_page .title input').value = '';
            $('.Making_page .text_box').innerHTML = '';
        }, true);
    }
    // 隐藏弹窗
    $('.Making_page .popup').style.display = 'none';
}
//点击进入编辑页面
$('.Making_page .popup_box button')[1].onclick = () => {
    // 如果标题和内容不为空
    if (newtitle != '' && newcontext != '') {
        //创建模板
        let poststr = `context=${newcontext}&userId=${curr.userId}&modleTitle=${newtitle}&overWrite=0&modleLabel=1`
        ajax(`http://8.134.104.234:8080/ReciteMemory/modle/MakeModle`, 'post', poststr, (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr);
            let modle = newstr.data.modle;
            newTPFlag = true;
            newTP(newtitle, newcontext, modle.modleId);
            $('.edit_page .title_name').value = newtitle;
            $('.edit_page .text_page').innerHTML = newcontext;
            $('.edit_page').style.left = '0';
            $('.Making_page .title input').value = '';
            $('.Making_page .text_box').innerHTML = '';
        }, true);
    }
    $('.Making_page .popup').style.display = 'none';
}
//新建模板到个人仓库
function newTP(title, context, modleId) {
    let li = document.createElement('li');
    li.innerHTML = `<div class="tp_inner">
                        <div class="modleId">${modleId}</div>
                        <div class="content">
                            <h3 class="title ellipsis">${title}</h3>
                            <div class="info ellipsis">${context}</div>
                        </div>
                        <div class="tip">
                            <div class="date">2022-10-15</div>
                            <div class="times">学习次数：2</div>
                        </div>
                        <div class="template_btn">
                            <div class="tp_btn edit">编辑</div>
                            <div class="tp_btn del">删除</div>
                        </div>
                    </div>`
    $('.my_base .base_lis').prepend(li);
    //刷新仓库
    $('.footer_nav li')[0].onclick();
}







