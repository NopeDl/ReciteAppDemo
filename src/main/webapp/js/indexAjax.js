//上传文件
let newtitle = null;
let newcontext = null;
let newlabel = null;
var newTPFlag = false;

//获取用户模板
ajax(`http://8.134.104.234:8080/ReciteMemory/modle/UserMemory?userId=${curr.userId}`, 'get', '', (str) => {
    let newstr = JSON.parse(str).msg;
    if (newstr.data.userModle) {
        let tparr = newstr.data.userModle;
        for (let x of tparr) {
            console.log(x);
            let newcon = x.content.replace(/<缩进>/g, '&nbsp;&nbsp;&nbsp;&nbsp;').replace(/<\/p>/g, '').replace(/<p>/g, '');
            if (x.MStatus == '0')
                newTP(x.modleTitle, newcon, x.modleId, labelId2(x.modleLabel), x.common,true);
            else
                newTP(x.modleTitle, newcon, x.modleId, labelId2(x.modleLabel), x.common,false);
        }
        $('.footer_nav li')[0].onclick();
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
    if (curr.userInfo.base64 == '') {
        return;
    } else {
        let img = 'data:image/jpeg;base64,' + curr.userInfo.base64;
        for (let x of $('.head_portrait img')) {
            x.src = curr.userInfo.base64;
        }
    }


})


//上传文件
$('.Making_page .header_left input').onchange = function(e) {
    let file = e.target.files[0];
    if (e.target.files.length != 0) {
        $('.Making_page .loading').style.display = 'block';
        let fd = new FormData($('.upload_form'));
        ajax(`http://8.134.104.234:8080/ReciteMemory/upload/parseContent?userId=${curr.userId}`, 'post', fd, (str) => {
            $('.Making_page .loading').style.display = 'none';
            let newstr = JSON.parse(str).msg;
            let context = newstr.data.context;
            let newcon = context.replace(/<\/p>/g, '').replace(/<p>/g, '');
            //将文件内容渲染到页面
            $('.Making_page .title input').value = file.name;
            $('.Making_page .text_box').innerHTML = newcon;
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

        //标签不能为空
        if ($('.Making_page .label_cont').innerText == '标签 ') {
            $('.Making_page .popup2 .popup_box').innerHTML = '请选择标签';
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
        newlabel = $('.Making_page .label_cont').innerHTML;
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
            let newcon = newcontext.replace(/&nbsp;&nbsp;&nbsp;&nbsp;/g, '<缩进>');
            let contect = encodeURI(newcon);
            let poststr = `context=${contect}&userId=${curr.userId}&modleTitle=${newtitle}&overWrite=0&modleLabel=${labelId1(newlabel)}`
            ajax(`http://8.134.104.234:8080/ReciteMemory/modle/MakeModle`, 'post', poststr, (str) => {
                let newstr = JSON.parse(str).msg;
                let modle = newstr.data.modle;
                newTP(newtitle, newcontext, modle.modleId, newlabel,0,true);
                //刷新仓库
                $('.footer_nav li')[0].onclick();
                MakingTP();
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
        let newcon = newcontext.replace(/&nbsp;&nbsp;&nbsp;&nbsp;/g, '<缩进>').replace(/<div>/g, '').replace(/<\/div>/g, '');
        let poststr = `context=${newcon}&userId=${curr.userId}&modleTitle=${newtitle}&overWrite=0&modleLabel=${labelId1(newlabel)}`
        ajax(`http://8.134.104.234:8080/ReciteMemory/modle/MakeModle`, 'post', poststr, (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr);
            let modle = newstr.data.modle;
            newTPFlag = true;
            newTP(newtitle, newcon, modle.modleId, newlabel, 0,true);
            //刷新仓库
            $('.footer_nav li')[0].onclick();
            $('.edit_page .title_name').value = newtitle;
            $('.edit_page .text_page').innerHTML = newcontext;
            $('.edit_page .label_cont').innerHTML = newlabel;
            $('.edit_page').style.left = '0';
            MakingTP();
        }, true);
    }
    $('.Making_page .popup').style.display = 'none';
}

//编辑页面回复默认
function MakingTP() {
    $('.Making_page .title input').value = '';
    $('.Making_page .text_box').innerHTML = '';
    $('.Making_page .label_cont').innerHTML = `<span>标签</span>
    <span class="icon iconfont icon-xiangxiajiantou"></span>`
}