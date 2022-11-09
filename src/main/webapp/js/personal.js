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

    if (fileList.length) {
        //通过window.URL.createObjectURL(files[0])获得一个http格式的url路径
        // imgUrl = window.URL.createObjectURL(fileList[0]); 

        let fd = new FormData();
        var reader = new FileReader();
        reader.readAsDataURL(fileList[0]);
        reader.onloadend = function (e) {
            $("#canvas_img").src = e.target.result;
            
            $('.CaptureAvatar_page').style.display = 'block';
            setTimeout(() => {
                loadCapture();
            }, 50);
        };

        //点击确认裁剪头像
        $('.canvas_ok').onclick = () => {
            let base64 = $('#canvas').toDataURL('image/jpeg', 1 || 0.8);
            let tofile = dataURLtoFile(base64, 'image', 'image/jpeg')
            for (let x of $('.head_portrait img')) {
                x.src = base64;
            }
            fd.append("image", tofile);
            ajax(`http://8.134.104.234:8080/ReciteMemory/upload/uploadImg?userId=${curr.userId}`, 'post', fd, (str) => {
                let newstr = JSON.parse(str).msg;
                console.log(newstr);
                $('.CaptureAvatar_page').style.display = 'none';
            }, false);
        }
    }
}

//将base64转换成文件
function dataURLtoFile(dataURL, fileName, fileType) {
    let arr = dataURL.split(','), mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], fileName, { type: fileType || 'image/jpg' });
}

