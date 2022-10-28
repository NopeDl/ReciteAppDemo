$('.personal .exit').onclick = () => {
    if (!curr.auto) {
        window.localStorage.clear();
    }
    location.href = './login.html';
}

let modifyNum = 0;
let modifyArr = ['修改昵称', '修改手机号', '修改密码'];
let modifyErrArr = ['昵称格式错误', '手机号格式错误', '密码格式错误']
for (let i = 0; i < $('.modify_lis').length; i++) {
    $('.modify_lis')[i].onclick = () => {
        $('.modify_succ').classList.remove('modify_succani');
        $('.modify_what').innerHTML = modifyArr[i];
        $('.personal .modify').style.left = '0';
        if (i < 2)
            $('.Modify_interface input').value = $('.modify_value')[i].innerHTML;
        modifyNum = i;
    }
}

$('.modify_title .left').onclick = () => {
    $('.personal .modify').style.left = '100%';
    $('.Modify_interface input').value = '';
}

let nameReg = /^[0-9a-zA-Z\u4e00-\u9fa5]{1,6}$/;
let phoneReg = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
let passwordReg = /^[a-zA-Z0-9_]{6,16}$/;
$('.modify .confirm').onclick = (e) => {
    //保存修改的内容
    let modify_value = $('.Modify_interface input').value;
    //修改昵称
    e.stopPropagation();
    console.log(modifyNum);
    if (modifyNum == 0 && nameReg.test(modify_value)) {
        ajax(`http://8.134.104.234:8080/ReciteMemory/user.do/ReMessage?nickName=${modify_value}&userId=${curr.userId}`, 'get', '', (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr, modifyNum);

            if (newstr.data.isSuccess) {
                for (let x of $('.idname'))
                    x.innerHTML = modify_value;
                $('.modify_title .left').onclick();
                $('.modify_succ').classList.add('modify_succani');
                curr.userInfo.nickName = modify_value;
                saveData('current_user', curr);
            } else {
                $('.modify_err').innerHTML = '用户名已被使用';
                $('.modify_err').style.opacity = '1';
            }

        }, true);
    }
    //修改手机号
    else if (modifyNum == 1 && phoneReg.test(modify_value)) {
        ajax(`http://8.134.104.234:8080/ReciteMemory/user.do/ReMessage?phone=${modify_value}&userId=${curr.userId}`, 'get', '', (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr, modifyNum);
            if (newstr.data.isSuccess) {
                $('.modify_value')[modifyNum].innerHTML = modify_value;
                $('.modify_title .left').onclick();
                $('.modify_succ').classList.add('modify_succani');
                curr.userInfo.phone = modify_value;
                saveData('current_user', curr);
            } else {
                $('.modify_err').innerHTML = '手机号已被注册';
                $('.modify_err').style.opacity = '1';
            }

        }, true);
    }
    //修改密码
    else if (modifyNum == 2 && passwordReg.test(modify_value)) {
        ajax(`http://8.134.104.234:8080/ReciteMemory/user.do/ReMessage?password=${modify_value}&userId=${curr.userId}`, 'get', '', (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr, modifyNum);
            if (newstr.data.isSuccess) {
                $('.modify_title .left').onclick();
                $('.modify_succ').classList.add('modify_succani');
            } else {
                $('.modify_err').innerHTML = '修改密码失败';
                $('.modify_err').style.opacity = '1';
            }
        }, true);
    } else {
        $('.modify_err').innerHTML = modifyErrArr[modifyNum];
        $('.modify_err').style.opacity = '1';
    }
}

$('.modify').onclick = () => {
    $('.modify_err').style.opacity = '0';
}

//上传头像 

$('.personal .head_portrait input').onchange = function (e) {
    const fileList = e.target.files;
    let fd = new FormData($('.img_form'));
    if (fileList.length) {
        //通过window.URL.createObjectURL(files[0])获得一个http格式的url路径
        imgUrl = window.URL.createObjectURL(fileList[0]);
        //设置img中的src进行显示
        $('.personal .head_portrait img').src = imgUrl;
        // var reader = new FileReader();
        // reader.readAsDataURL(fileList[0])   //将读取的文件转换成base64格式

        // reader.onload = function (e) {
        //     let base64Data = e.target.result;
        //     // var newBase64 = base64Data.replace(/\+/g, "%2B");
        //     let newBase64 = base64Data.replace(/^data:image\/\w+;base64,/, "");
        //     console.log(base64Data);

        // }

        ajax(`http://8.134.104.234:8080/ReciteMemory/upload/uploadImg?userId=${curr.userId}`, 'post', fd, (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr);
        }, false);
    }
}
