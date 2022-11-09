let interactive = document.querySelectorAll(".inter_box");

var dainzan = document.querySelectorAll(".dainzan");
let jifen = document.querySelectorAll(".jifen");

var menu = document.querySelectorAll(".menu");
let commonArr = [];


//封装社区模板所有事件函数
function communityTP() {
    //社区模板点击菜单出现
    Array.from(all('.menu')).forEach((x, i) => {
        x.addEventListener('click', function (event) {
            for (let k of all('.inter_box')) {
                k.style.width = "0";
            }
            if (all('.inter_box')[i].offsetWidth == '0') {
                all('.inter_box')[i].style.width = "44vw";
            } else {
                all('.inter_box')[i].style.width = "0";
            }
            event.stopPropagation(); //阻止冒泡
        })
    })
    //点击收藏
    Array.from(all('.community_ul .dainzan')).forEach((x, i) => {
        for (let k of all('.collection_base .modleId')) {
            if (k.innerHTML == all('.community_ul .modleId')[i].innerHTML) {
                x.classList.add("yellow");
            }
        }

        x.addEventListener('click', function (event) {
            //如果模板被收藏
            if (x.classList.contains('yellow')) {
                //对比循环收藏库中的模板找出对应模板
                for (let k of all('.collection_base .modleId')) {
                    if (k.innerHTML == all('.community_ul .modleId')[i].innerHTML) {
                        ajax(`http://8.134.104.234:8080/ReciteMemory/modle/Collection?userId=${curr.userId}&modleId=${k.innerHTML}&mStatus=0`, 'get', '', (str) => {
                            let newstr = JSON.parse(str).msg;
                            console.log(newstr);

                            $('.collection_base .base_lis').removeChild(k.parentNode.parentNode);
                            //刷新仓库
                            resetbase();
                        }, true);

                    }
                }
                // 模板未收藏
            } else {
                ajax(`http://8.134.104.234:8080/ReciteMemory/modle/Collection?userId=${curr.userId}&modleId=${all('.community_ul .modleId')[i].innerHTML}&mStatus=1`, 'get', '', (str) => {
                    let newstr = JSON.parse(str).msg;
                    console.log(newstr);
                    newTP(all('.community_ul .title')[i].innerHTML, all('.community_ul .info')[i].innerHTML, all('.community_ul .modleId')[i].innerHTML, all('.community_ul .label')[i].querySelectorAll('span')[1].innerHTML, 1,'未学习',false);
                    //刷新仓库
                    resetbase();
                }, true);

            }

            x.classList.toggle("yellow");
            setTimeout(() => all('.inter_box')[i].style.width = "0", 600);
            event.stopPropagation(); //阻止冒泡

        })
    })
    //点赞
    Array.from(all('.jifen')).forEach((x, i) => {
        x.addEventListener('click', function (event) {
            if (x.classList.contains("yellow")) {
                x.classList.remove("yellow");
            } else {
                x.classList.add("yellow");
            }
            setTimeout(() => all('.inter_box')[i].style.width = "0", 600);
            event.stopPropagation(); //阻止冒泡
        })
    })
    //点击页面隐藏菜单
    document.addEventListener('click', function (event) {
        for (let k of all('.inter_box')) {
            k.style.width = "0";
        }
        event.stopPropagation(); //阻止冒泡

    });
    //点击浏览模板
    Array.from(all('.community_ul .content')).forEach((x, i) => {
        x.addEventListener('click', function (event) {
            if (x.nextElementSibling.querySelector('.dainzan').classList.contains('yellow')) {
                $('.viewTemplate .dainzan .iconfont').classList.add('yellow');
            }
            $('.viewTemplate footer').style.display = 'block';
            if (x.parentNode.querySelector('.idname1').innerHTML == curr.userInfo.nickName) {
                // $('.viewTemplate footer').style.display = 'none';
                $('.viewTemplate footer .dainzan .iconfont').classList.add('icon-a-shanchulajitong');
                $('.viewTemplate footer .dainzan .iconfont').classList.remove('icon-shoucang');
                $('.vt_text').innerHTML = '删除';
            }else{
                $('.viewTemplate footer .dainzan .iconfont').classList.remove('icon-a-shanchulajitong');
                $('.viewTemplate footer .dainzan .iconfont').classList.add('icon-shoucang');
                $('.vt_text').innerHTML = '收藏';
            }
            $('.viewTemplate').classList.remove('scroll_top');
            $('.viewTemplate footer').classList.remove('scroll_top');
            $('.viewTemplate .idname').innerHTML = all('.community_ul .idname1')[i].innerHTML;
            $('.viewTemplate .title').innerHTML = all('.community_ul .title')[i].innerHTML;
            $('.viewTemplate .text_box').innerHTML = all('.community_ul .info')[i].innerHTML;
            $('.viewTemplate .label').innerHTML = all('.community_ul .label')[i].innerHTML;
            $('.viewTemplate .head_portrait').innerHTML = all('.community_ul .head_portrait')[i].innerHTML;
            event.stopPropagation(); //阻止冒泡

            //浏览模板点击取消收藏
            $('.viewTemplate .dainzan').onclick = () => {
                //如果模板被收藏
                if ($('.viewTemplate .dainzan .iconfont').classList.contains('yellow')) {
                    //对比循环收藏库中的模板找出对应模板
                    for (let k of all('.collection_base .modleId')) {
                        if (k.innerHTML == all('.community_ul .modleId')[i].innerHTML) {
                            ajax(`http://8.134.104.234:8080/ReciteMemory/modle/Collection?userId=${curr.userId}&modleId=${k.innerHTML}&mStatus=0`, 'get', '', (str) => {
                                let newstr = JSON.parse(str).msg;
                                console.log(newstr);
                                $('.collection_base .base_lis').removeChild(k.parentNode.parentNode);
                                //刷新仓库
                                resetbase();
                            }, true);

                        }
                    }
                    // 模板未收藏
                } else {
                    ajax(`http://8.134.104.234:8080/ReciteMemory/modle/Collection?userId=${curr.userId}&modleId=${all('.community_ul .modleId')[i].innerHTML}&mStatus=1`, 'get', '', (str) => {
                        let newstr = JSON.parse(str).msg;
                        console.log(newstr);
                        newTP(all('.community_ul .title')[i].innerHTML, all('.community_ul .info')[i].innerHTML, all('.community_ul .modleId')[i].innerHTML, all('.community_ul .label')[i].querySelectorAll('span')[1].innerHTML, false);
                        //刷新仓库
                        resetbase();
                    }, true);
                }
                $('.viewTemplate .dainzan .iconfont').classList.toggle("yellow");
                if ($('.viewTemplate .dainzan .iconfont').classList.contains('yellow')) {
                    all('.community_ul .dainzan')[i].classList.add('yellow');
                    // x.nextElementSibling.querySelector('.dainzan').classList.add('yellow');
                } else {
                    all('.community_ul .dainzan')[i].classList.remove('yellow');
                    // x.nextElementSibling.querySelector('.dainzan').classList.remove('yellow');
                }

            }
        })

    })

    //点击删除上传的模板
    Array.from(all('.community_ul .shanchu')).forEach((x, i) => {
        x.addEventListener('click', function (event) {
            if (x.classList.contains("yellow")) {
                x.classList.remove("yellow");
            } else {
                x.classList.add("yellow");
            }
            event.stopPropagation(); //阻止冒泡
            let modleId = x.parentNode.parentNode.parentNode.parentNode.previousElementSibling.innerHTML;
            console.log(modleId);
            ajax(`http://8.134.104.234:8080/ReciteMemory/modle/toCommunity`, 'post', `modleId=${modleId}&common=${0}`, (str) => {
                let newstr = JSON.parse(str).msg;
                console.log(newstr);
                if (newstr.code == '200') {
                    for (let k of all('.my_base .tp_inner')) {
                        if (k.innerHTML == modleId) {
                            k.querySelector('.common').innerHTML = '0';
                        }
                    }
                    xrcomTP();
                    $('.community header .label li')[0].onclick();
                }
            }, true);

        })
    })

}
communityTP()
//浏览页面点击返回
$('.viewTemplate .back').onclick = () => {
    $('.viewTemplate').classList.add('scroll_top');
    $('.viewTemplate footer').classList.add('scroll_top');
    $('.viewTemplate .dainzan .iconfont').classList.remove('yellow');
}
//渲染社区模板
function xrcomTP() {
    $('.community_ul').innerHTML = '<li class="footer"></li>';
    commonArr = [];
    commonArr[0] = [];
    let timer;
    for (let i = 1; i <= 3; i++) {
        ajax(`http://8.134.104.234:8080/ReciteMemory/inf.get/getModlesByTag?modleLabel=${i}&pageIndex=0`, 'get', '', (str) => {
            let newstr = JSON.parse(str).msg;
            console.log(newstr);
            if (newstr.content != '参数获取失败') {
                let comarr = [];
                for (let x of newstr.data.modleList) {
                    if (x.common != 0) {
                        comarr.push(x);
                    }
                }
                commonArr[i] = comarr;
                console.log(comarr, commonArr);
                if (comarr.length != 0) {
                    for (let x of comarr) {
                        if(x.userId == 50)
                            commonArr[0].push(x);
                    }
                }
            }
            communityTP();
            $('.community header .label li')[0].onclick();
            $('.com_loading').style.display = 'none';
        }, true);
    }
}
xrcomTP();



//点击进入上传页面
$('.community .upload').onclick = () => {
    $('.upload_page').classList.remove('scroll_top');
    for (let x of all('.my_base .tp_inner')) {
        let modleId = x.querySelector('.modleId').innerHTML;
        let title = x.querySelector('.title').innerHTML;
        let context = x.querySelector('.info').innerHTML;
        let label = x.querySelector('.label').innerHTML;
        let common = x.querySelector('.common').innerHTML;
        UploadTP(title, context, modleId, label, common);
    }
    setTimeout(() => {
        for (let x of all('.upload_page li')) {
            x.classList.add('uploadLis_fadeIn');
        }
        for (let x of all('.upload_page .select .circle')) {
            x.onclick = () => {
                x.classList.toggle('selected');
            }
        }
    }, 50);

}

//上传到社区
$('.uploadMP').onclick = () => {
    Array.from(all('.upload_page .select .circle')).forEach((x, i) => {
        let modleId = all('.upload_page li')[i].querySelector('.modleId').innerHTML;
        if (x.classList.contains('selected')) {
            ajax(`http://8.134.104.234:8080/ReciteMemory/modle/toCommunity`, 'post', `modleId=${modleId}&common=1`, (str) => {
                let newstr = JSON.parse(str).msg;
                console.log(newstr);
                if (newstr.code == '200') {
                    $('.upload_page .back').onclick();
                }
            }, true);
        } else {
            ajax(`http://8.134.104.234:8080/ReciteMemory/modle/toCommunity`, 'post', `modleId=${modleId}&common=0`, (str) => {
                let newstr = JSON.parse(str).msg;
                console.log(newstr);
                if (newstr.code == '200') {
                    $('.upload_page .back').onclick();
                }
            }, true);
        }

        for (let k of all('.my_base .tp_inner')) {
            if (x.classList.contains('selected')) {
                k.querySelector('.common').innerHTML = '1';
            } else {
                k.querySelector('.common').innerHTML = '0';
            }
        }

        if (i == all('.upload_page .select .circle').length - 1) {
            $('.community_ul').innerHTML = '<li class="footer"></li>';
            xrcomTP();
        }

    })
}
//上传页面点击返回
$('.upload_page .back').onclick = () => {
    $('.upload_page').classList.add('scroll_top');
    $('.upload_page ul').innerHTML = ''
}

//切换标签
Array.from($('.community header .label li')).forEach((x, i) => {
    x.onclick = () => {
        $('.community_ul').innerHTML = '<li class="footer"></li>';
        for (let k of $('.community header .label li')) {
            k.classList.remove('active');
        }
        x.classList.add('active');
        if (i == 0 && commonArr[0].length != 0) {
            for (let k of commonArr[0]) {
                let name_flag = true;
                if (k.nickName == curr.userInfo.nickName)
                    name_flag = false;
                let newcont = k.content.replace(/<空格>/g, '&nbsp;');

                comTP(k.modleTitle, newcont, k.modleId, k.modleLabel, k.base64,k.nickName, name_flag);
            }
        }
        if (commonArr[labelId1(x.innerText)] && i != 0) {
            for (let k of commonArr[labelId1(x.innerText)]) {
                let name_flag = true;
                if (k.nickName == curr.userInfo.nickName)
                    name_flag = false;
                let newcont = k.content.replace(/<空格>/g, '&nbsp;');
                comTP(k.modleTitle, newcont, k.modleId, k.modleLabel, k.base64,k.nickName, name_flag);
            }
        }
        communityTP();
    }
})