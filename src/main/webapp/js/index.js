let curr = getData('current_user');

//底部导航栏
for (let i = 0; i < $('.footer_nav li').length; i++) {
    $('.footer_nav li')[i].onclick = () => {
        for (let x of $('.footer_nav li'))
            x.style.color = '#9fa7ba'
        $('.footer_nav li')[i].style.color = '#5c4eaf';
        pageReset(i);
        if (i == 0) {
            $('.memory_base header').classList.remove('scroll_top');
        } else if (i == 1) {
            $('.pk_page .pk_footer').classList.remove('scroll_top');
        }
        if (i == 3) {
            $('.community header').classList.remove('scroll_top');
            $('.community header .label li')[0].onclick();
        }

        $('.main_page')[i].classList.remove('scroll_top');

    }
}
$('.footer_nav li')[0].onclick();
function pageReset(i) {
    $('.memory_base header').classList.add('scroll_top');
    $('.pk_page .pk_footer').classList.add('scroll_top');
    $('.community header').classList.add('scroll_top');
    for (let x of $('.main_page')) {
        x.classList.add('scroll_top');
    }
    if (i == 0) {
        resetbase();
        TP();
    }

}
// 刷新仓库
function resetbase() {
    if (document.querySelector('.my_base li')) {
        for (let x of all('.my_base li'))
            x.classList.add('baseLis_fadeIn');
        $('.base_none_1').style.display = 'none';
    } else {
        $('.base_none_1').style.display = 'block';
    }
    if (document.querySelector('.collection_base li')) {
        for (let x of all('.collection_base li'))
            x.classList.add('baseLis_fadeIn');
        $('.base_none_2').style.display = 'none';
    } else {
        $('.base_none_2').style.display = 'block';
    }
}
//模板向左滑动出现按钮
var title = null;
var info = null;
var modleId = null;
var label = null;
var flag_learn = false;
var mStatus = 0;
//为模板添加事件

function TP() {
    Array.from(all('.tp_inner')).forEach((x, i) => {
        x.ontouchstart = (e) => {
            let l = 0;
            e.stopPropagation();
            // 点击模板将所有模板left改为0
            tp_inner_left();
            let disX = e.changedTouches[0].clientX;
            // 滑动模板出现按钮
            x.addEventListener('touchmove', function (e) {
                l = e.changedTouches[0].clientX - disX;
                if (l < -20)
                    x.style.left = '-40vw';
                if (l > 10)
                    x.style.left = '0';
            })
            //如果没有滑动则进入学习页面
            x.parentNode.ontouchend = () => {
                $('.learn_page .title_name').innerHTML = all('.tp_inner .title')[i].innerHTML;
                $('.learn_page .text_box').innerHTML = all('.tp_inner .info')[i].innerHTML;
                $('.learn_page .label').innerHTML = all('.tp_inner .label')[i].querySelectorAll('span')[1].innerHTML;
                if (l == 0 && x.offsetLeft == '0') {
                    $('.learn_page').style.left = '0';
                    flag_learn = true;
                }
            }
            //点击编辑进入编辑页面
            all('.template_btn .edit')[i].onclick = (e) => {
                title = all('.tp_inner .title')[i];
                info = all('.tp_inner .info')[i];
                modleId = all('.tp_inner .modleId')[i];
                label = all('.tp_inner .label')[i];
                console.log(label);
                if (all('.memory_base li')[i].parentNode.parentNode.classList.contains('collection_base'))
                    mStatus = 1;
                $('.edit_page .title_name').value = all('.tp_inner .title')[i].innerHTML;
                $('.edit_page .text_page').innerHTML = all('.tp_inner .info')[i].innerHTML;
                $('.edit_page .label_cont').innerHTML = all('.tp_inner .label')[i].querySelectorAll('span')[1].innerHTML;
                $('.edit_page').style.left = '0';
                e.stopPropagation();
            }

            //点击删除模板
            all('.template_btn .del')[i].onclick = (e) => {

                x.parentNode.classList.add('baseLis_del');
                e.stopPropagation();
                if (all('.my_base li').length == all('.my_base .baseLis_del').length)
                    $('.base_none_1').style.display = 'block';
                if (all('.collection_base li').length == all('.collection_base .baseLis_del').length)
                    $('.base_none_2').style.display = 'block';
                let modleId = all('.modleId')[i].innerHTML;
                if (i < all('.my_base li').length) {
                    ajax(`http://8.134.104.234:8080/ReciteMemory/modle/deleteModle`, 'post', `modleId=${modleId}`, (str) => {
                        let newstr = JSON.parse(str).msg;
                        console.log(newstr);
                        x.parentNode.addEventListener('animationend', () => {
                            $('.my_base ul').removeChild(x.parentNode);
                        })
                    }, true)
                } else {
                    
                    ajax(`http://8.134.104.234:8080/ReciteMemory/modle/CancelCollet?userId=${curr.userId}&modleId=${all('.tp_inner')[i].querySelector('.modleId').innerHTML}&mStatus=0`, 'get','', (str) => {
                        let newstr = JSON.parse(str).msg;
                        console.log(newstr);
                        x.parentNode.addEventListener('animationend', () => {
                            $('.collection_base ul').removeChild(x.parentNode);
                        })
                    }, true)
                }
            }
        }
    });
}
TP();

//点击页面收起模板按钮
document.ontouchstart = () => tp_inner_left();

function tp_inner_left() {
    for (let k of all('.tp_inner'))
        k.style.left = '0';
}

// 点击切换收藏
let timer = null;
$('.icon_btn').onclick = () => {
    if (!document.querySelector('.collection_base li'))
        $('.base_none_2').style.display = 'block';

    timer = null;
    if (document.querySelector('.my_base li')) {
        for (let x of all('.my_base li'))
            x.classList.add('baseLis_fadeIn');
        $('.base_none_1').style.display = 'none';
    } else {
        $('.base_none_1').style.display = 'block';
    }
    if (document.querySelector('.collection_base li')) {
        for (let x of all('.collection_base li'))
            x.classList.add('baseLis_fadeIn');
        $('.base_none_2').style.display = 'none';
    } else {
        $('.base_none_2').style.display = 'block';
    }
    TP();
    timer = setTimeout(() => {
        if ($('.icon_btn .icon').classList.contains('icon-shoucang')) {
            $('.icon_btn .icon').classList.add('icon-jiyi');
            $('.icon_btn .icon').classList.remove('icon-shoucang');
            $('.my_base').style.transform = 'scale(0)';
            $('.collection_base').style.transform = 'scale(1)';
        } else {
            $('.icon_btn .icon').classList.remove('icon-jiyi');
            $('.icon_btn .icon').classList.add('icon-shoucang');
            $('.my_base').style.transform = 'scale(1)';
            $('.collection_base').style.transform = 'scale(0)';
        }

    }, 500);
    $('.collection_base').classList.toggle('clip_path_change');
    $('.icon_btn').classList.add('change_ani');
    $('.icon_btn').addEventListener('animationend', () => {
        $('.icon_btn').classList.remove('change_ani');
        for (let x of all('.collection_base li')) {
            x.classList.add('baseLis_fadeIn');
        }

    })
}



//点击返回隐藏学习页面
$('.learn_page .header_left').onclick = () => {
    $('.learn_page').style.left = '100%';
}

//点击出现打卡页面
$('.memory_base .header_left').onclick = () => {
    $('.calendar_page').style.left = '0';
}

//打卡页面点击返回
$('.calendar_page .left').onclick = () => {
    $('.calendar_page').style.left = '-100%'
}


let curr1 = getData('current_user');
if (curr1.length == 0) {
    location.href = './login.html';
}